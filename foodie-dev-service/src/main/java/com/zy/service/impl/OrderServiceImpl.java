package com.zy.service.impl;

import com.zy.enums.OrderStatusEnum;
import com.zy.enums.YesOrNo;
import com.zy.idworker.Sid;
import com.zy.mapper.*;
import com.zy.pojo.*;
import com.zy.pojo.bo.ShopcartBO;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.pojo.vo.MerchantOrdersVO;
import com.zy.pojo.vo.OrderVO;
import com.zy.service.ItemService;
import com.zy.service.OrderService;
import com.zy.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired(required = false)
    private UserAddressMapper userAddressMapper;
    @Autowired(required = false)
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired(required = false)
    private OrdersMapper ordersMapper;
    @Autowired(required = false)
    private OrderItemsMapper orderItemsMapper;
    @Autowired(required = false)
    private OrderStatusMapper orderStatusMapper;
    @Autowired(required = false)
    private ItemsImgMapper itemsImgMapper;
    @Autowired(required = false)
    private ItemService itemService;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(List<ShopcartBO> shopCartList, SubmitOrderBO submitOrderBO) throws Exception {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Map<String, Object> param = new HashMap<>();
        param.put("isMain", YesOrNo.YES.type);
        //订单id
        String orderId = Sid.nextShort();
        //包邮
        int postAmount = 0;
        //订单总价格
        int totalAmount = 0;
        //实际支付总价格
        int realPayAmount = 0;

        //根据规格ids获取商品信息
        String[] specIds = itemSpecIds.split(",");
        List<ShopcartBO> toBeRemovedShopcastList = new ArrayList<>();
        for (String specId : specIds) {
            ShopcartBO cartItem = this.getCount(shopCartList, specId);
            if (cartItem == null) {
                continue;
            }
            toBeRemovedShopcastList.add(cartItem);
            int buyCount = cartItem.getBuyCounts();
            OrderItems orderItems = new OrderItems();
            ItemsSpec itemsSpec = itemsSpecMapper.getItemsSpecById(specId);
            param.put("itemId", itemsSpec.getItemId());
            List<ItemsImg> itemsImgList = itemsImgMapper.getItemsImgListByMap(param);
            if (itemsImgList != null || itemsImgList.size() != 0) {
                orderItems.setItemImg(itemsImgList.get(0).getUrl());
            }
            totalAmount += (itemsSpec.getPriceNormal() * buyCount);
            realPayAmount += (itemsSpec.getPriceDiscount() * buyCount);
            // 整合redis后商品购买的数量从新从redis购物车中获取 目前先写死
            //构建订单商品关联对象
            orderItems.setId(Sid.nextShort());
            orderItems.setOrderId(orderId);
            orderItems.setItemId(itemsSpec.getItemId());
            orderItems.setItemName(itemService.queryItemById(itemsSpec.getItemId()).getItemName());
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setItemSpecId(itemsSpec.getId());
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItems.setBuyCounts(buyCount);
            //入库
            orderItemsMapper.insertOrderItems(orderItems);

            //提交订单之后规格表需要扣除相应的库存
            itemService.updateItemsSpecStock(specId, buyCount);
        }

        UserAddress userAddress = userAddressMapper.getUserAddressById(addressId);
        //构建订单对象
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity()
                + " " + userAddress.getDistrict() + " " + userAddress.getDetail());
        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);
        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        //订单对象入库
        ordersMapper.insertOrders(orders);

        //构建订单状态对象
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(orders.getCreatedTime());
        orderStatusMapper.insertOrderStatus(orderStatus);

        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);
        // 5. 构建自定义订单vo
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcastList(toBeRemovedShopcastList);
        return orderVO;
    }

    private ShopcartBO getCount(List<ShopcartBO> shopCartList,String specId){
        for (ShopcartBO shopcartBO : shopCartList) {
            if (shopcartBO.getSpecId().equals(specId)) {
                return shopcartBO;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) throws Exception {
        OrderStatus order = new OrderStatus();
        order.setOrderId(orderId);
        order.setPayTime(new Date());
        order.setOrderStatus(orderStatus);
        orderStatusMapper.updateOrderStatus(order);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) throws Exception {
        return orderStatusMapper.getOrderStatusById(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() throws Exception {
        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        Map<String, Object> param = new HashMap<>();
        param.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatus = orderStatusMapper.getOrderStatusListByMap(param);
        if (orderStatus != null) {
            for (OrderStatus status : orderStatus) {
                //获取订单时间，并与当前时间做对比
                Date createdTime = status.getCreatedTime();
                int days = DateUtil.daysBetween(createdTime, new Date());
                if (days >= 1) {
                    //订单超过一天则关闭订单
                    this.doCloseOrder(status.getOrderId());
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) throws Exception {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateOrderStatus(close);
    }

}
