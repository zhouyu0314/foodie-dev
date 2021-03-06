package com.zy.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.zy.enums.OrderStatusEnum;
import com.zy.enums.YesOrNo;
import com.zy.mapper.OrderStatusMapper;
import com.zy.mapper.OrdersMapper;
import com.zy.pojo.OrderStatus;
import com.zy.pojo.Orders;
import com.zy.pojo.vo.MyOrdersVO;
import com.zy.pojo.vo.OrderStatusCountsVO;
import com.zy.service.base.BaseServiceImpl;
import com.zy.service.center.MyOrdersService;
import com.zy.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl extends BaseServiceImpl implements MyOrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("orderStatus", orderStatus);

        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapper.queryMyOrders(param);
        return this.setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) throws Exception {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());
        orderStatusMapper.updateOrderStatus(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("id", orderId);
        param.put("userId", userId);
        param.put("isDelete", YesOrNo.NO.type);
        List<Orders> list = ordersMapper.getOrdersListByMap(param);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) throws Exception {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());
        Integer row = orderStatusMapper.updateOrderStatus(orderStatus);
        if (row != 1) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) throws Exception {
        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);
        order.setIsDelete(YesOrNo.YES.type);
        order.setUpdatedTime(new Date());
        Integer row = ordersMapper.updateOrders(order);
        if (row != 1) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getMyOrderStatusCounts(String userId) throws Exception {
        Map<String, Object> param = new HashMap<>();

        param.put("userId", userId);
        param.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapper.getMyOrderStatusCounts(param);

        param.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapper.getMyOrderStatusCounts(param);

        param.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapper.getMyOrderStatusCounts(param);

        param.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        param.put("isComment", YesOrNo.NO.type);
        int waitSuccessCounts = ordersMapper.getMyOrderStatusCounts(param);

        return new OrderStatusCountsVO(waitPayCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitSuccessCounts);


    }

    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) throws Exception{

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = ordersMapper.getMyOrderTrend(map);

        return setterPagedGrid(list, page);
    }


}
