package com.zy.service;

import com.zy.pojo.OrderStatus;
import com.zy.pojo.bo.ShopcartBO;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.pojo.vo.OrderVO;

import java.util.List;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderBO
     */
    OrderVO createOrder(List<ShopcartBO> shopCartList, SubmitOrderBO submitOrderBO) throws Exception;

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus) throws Exception;

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId) throws Exception;

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder() throws Exception;




}
