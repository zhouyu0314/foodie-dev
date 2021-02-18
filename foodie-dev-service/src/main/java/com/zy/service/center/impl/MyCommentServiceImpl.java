package com.zy.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.zy.enums.YesOrNo;
import com.zy.idworker.Sid;
import com.zy.mapper.ItemsCommentsMapper;
import com.zy.mapper.OrderItemsMapper;
import com.zy.mapper.OrderStatusMapper;
import com.zy.mapper.OrdersMapper;
import com.zy.pojo.OrderItems;
import com.zy.pojo.OrderStatus;
import com.zy.pojo.Orders;
import com.zy.pojo.bo.center.OrderItemsCommentBO;
import com.zy.pojo.vo.MyCommentVO;
import com.zy.service.base.BaseServiceImpl;
import com.zy.service.center.MyCommentService;
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
public class MyCommentServiceImpl extends BaseServiceImpl implements MyCommentService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("orderId", orderId);
        return orderItemsMapper.getOrderItemsListByMap(param);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) throws Exception {
        //1.保存评价 items_comments
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(Sid.nextShort());
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("commentList", commentList);
        itemsCommentsMapper.insertItemsComments(param);
        //2.修改订单表已评价 orders
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.NO.type);
        ordersMapper.updateOrders(orders);
        //3.修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateOrderStatus(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments( String userId, Integer page, Integer pageSize) throws Exception {
        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapper.queryMyComments(param);
        return this.setterPagedGrid(list, page);
    }
}
