package com.zy.mapper;
import com.zy.pojo.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderStatusMapper {

	OrderStatus getOrderStatusById(@Param(value = "orderId") String id)throws Exception;

	List<OrderStatus>	getOrderStatusListByMap(Map<String, Object> param)throws Exception;

	Integer getOrderStatusCountByMap(Map<String, Object> param)throws Exception;

	Integer insertOrderStatus(OrderStatus orderStatus)throws Exception;

	Integer updateOrderStatus(OrderStatus orderStatus)throws Exception;


}
