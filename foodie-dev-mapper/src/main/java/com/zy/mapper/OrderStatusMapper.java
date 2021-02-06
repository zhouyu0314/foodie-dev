package com.zy.mapper;
import com.zy.entity.OrderStatus;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderStatusMapper {

	public OrderStatus getOrderStatusById(@Param(value = "id") Long id)throws Exception;

	public List<OrderStatus>	getOrderStatusListByMap(Map<String, Object> param)throws Exception;

	public Integer getOrderStatusCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertOrderStatus(OrderStatus orderStatus)throws Exception;

	public Integer updateOrderStatus(OrderStatus orderStatus)throws Exception;


}
