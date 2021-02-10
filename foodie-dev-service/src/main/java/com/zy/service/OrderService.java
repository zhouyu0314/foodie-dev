package com.zy.service;

import com.zy.pojo.OrderStatus;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.pojo.vo.OrderVO;

public interface OrderService {
    /**
     * 用于创建订单相关信息
     *
     * @param submitOrderBO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) throws Exception;

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus) throws Exception;

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId) throws Exception;

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder() throws Exception;

}
