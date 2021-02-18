package com.zy.service.center;

import com.zy.pojo.OrderItems;
import com.zy.pojo.bo.center.OrderItemsCommentBO;
import com.zy.utils.PagedGridResult;

import java.util.List;

public interface MyCommentService {

    /**
     * 根据订单id查询关联的商品
     * @param orderId
     * @return
     * @throws Exception
     */
    List<OrderItems> queryPendingComment(String orderId)throws Exception;


    void saveComments(String orderId,String userId, List<OrderItemsCommentBO> commentList)throws Exception;

    PagedGridResult queryMyComments( String userId, Integer page,Integer pageSize)throws Exception;
}
