package com.zy.mapper;

import com.zy.pojo.OrderStatus;
import com.zy.pojo.Orders;
import com.zy.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    Orders getOrdersById(@Param(value = "id") String id) throws Exception;

    List<Orders> getOrdersListByMap(Map<String, Object> param) throws Exception;

    Integer getOrdersCountByMap(Map<String, Object> param) throws Exception;

    Integer insertOrders(Orders orders) throws Exception;

    Integer updateOrders(Orders orders) throws Exception;

    List<MyOrdersVO> queryMyOrders(Map<String, Object> param) throws Exception;

    int getMyOrderStatusCounts(Map<String, Object> param) throws Exception;

    List<OrderStatus> getMyOrderTrend(Map<String, Object> param)throws Exception;


}
