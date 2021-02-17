package com.zy.service.center;

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

    void updateDeliverOrderStatus(String orderId)throws Exception;
}
