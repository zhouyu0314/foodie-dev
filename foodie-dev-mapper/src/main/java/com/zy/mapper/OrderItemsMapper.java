package com.zy.mapper;
import com.zy.entity.OrderItems;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemsMapper {

	public OrderItems getOrderItemsById(@Param(value = "id") Long id)throws Exception;

	public List<OrderItems>	getOrderItemsListByMap(Map<String, Object> param)throws Exception;

	public Integer getOrderItemsCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertOrderItems(OrderItems orderItems)throws Exception;

	public Integer updateOrderItems(OrderItems orderItems)throws Exception;


}
