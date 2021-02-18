package com.zy.service.center;

import com.zy.pojo.Orders;
import com.zy.utils.PagedGridResult;

public interface MyOrdersService {
    /**
     * 查询订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    PagedGridResult queryMyOrders(String userId,Integer orderStatus,Integer page,Integer pageSize)throws Exception;

    /**
     * 商家发货，订单状态
     * @param orderId
     * @throws Exception
     */
    void updateDeliverOrderStatus(String orderId)throws Exception;


    /**
     * 查询订单
     * @param userId
     * @param orderId
     * @return
     * @throws Exception
     */
    Orders queryMyOrder(String userId, String orderId)throws Exception;

    /**
     * 更新订单状态，确认收获
     * @param orderId
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId)throws Exception ;

    /**
     * 更新订单状态，删除订单（逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId,String orderId)throws Exception ;
}
