package com.zy.mapper;
import com.zy.entity.Items;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemsMapper {

	public Items getItemsById(@Param(value = "id") Long id)throws Exception;

	public List<Items>	getItemsListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItems(Items items)throws Exception;

	public Integer updateItems(Items items)throws Exception;


}
