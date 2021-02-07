package com.zy.mapper;
import com.zy.pojo.Items;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsMapper {

	public Items getItemsById(@Param(value = "id") String id)throws Exception;

	public List<Items>	getItemsListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItems(Items items)throws Exception;

	public Integer updateItems(Items items)throws Exception;


}
