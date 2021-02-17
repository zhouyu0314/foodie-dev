package com.zy.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.zy.enums.OrderStatusEnum;
import com.zy.mapper.OrderStatusMapper;
import com.zy.mapper.OrdersMapper;
import com.zy.pojo.OrderStatus;
import com.zy.pojo.vo.MyOrdersVO;
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
public class MyOrdersServiceImpl extends BaseServiceImpl implements MyOrdersService  {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("orderStatus",orderStatus);

        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapper.queryMyOrders(param);
        return this.setterPagedGrid(list, page);
    }

    @Override
    public void updateDeliverOrderStatus(String orderId) throws Exception {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        orderStatus.setDeliverTime(new Date());
        orderStatusMapper.updateOrderStatus(orderStatus);
    }


}
