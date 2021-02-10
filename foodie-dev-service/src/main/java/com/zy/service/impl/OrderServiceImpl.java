package com.zy.service.impl;

import com.zy.enums.OrderStatusEnum;
import com.zy.enums.YesOrNo;
import com.zy.idworker.Sid;
import com.zy.mapper.*;
import com.zy.pojo.*;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.pojo.vo.OrderVO;
import com.zy.service.ItemService;
import com.zy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemService itemService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) throws Exception {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        Map<String,Object> param = new HashMap<>();
        param.put("isMain",YesOrNo.YES.type);
        //订单id
        String orderId = Sid.nextShort();
        //包邮
        int postAmount = 0;
        //订单总价格
        int totalAmount = 0;
        //实际支付总价格
        int realPayAmount = 0;
        int buyCount = 1;
        //根据规格ids获取商品信息
        String[] specIds = itemSpecIds.split(",");
        for (String specId : specIds) {
            OrderItems orderItems = new OrderItems();
            ItemsSpec itemsSpec = itemsSpecMapper.getItemsSpecById(specId);
            param.put("itemId",itemsSpec.getItemId());
            List<ItemsImg> itemsImgList = itemsImgMapper.getItemsImgListByMap(param);
            if (itemsImgList != null || itemsImgList.size() != 0) {
                orderItems.setItemImg(itemsImgList.get(0).getUrl());
            }
            totalAmount += (itemsSpec.getPriceNormal() * buyCount);
            realPayAmount += (itemsSpec.getPriceDiscount() * buyCount);
            // TODO: 2021/2/10 整合redis后商品购买的数量从新从redis购物车中获取 目前先写死
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
            itemService.updateItemsSpecStock(specId,buyCount);
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


        return null;
    }

    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {

    }

    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return null;
    }

    @Override
    public void closeOrder() {

    }
}
