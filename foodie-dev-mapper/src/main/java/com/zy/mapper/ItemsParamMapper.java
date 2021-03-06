package com.zy.mapper;
import com.zy.pojo.ItemsParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsParamMapper {

	public ItemsParam getItemsParamById(@Param(value = "id") Long id)throws Exception;

	public List<ItemsParam>	getItemsParamListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsParamCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsParam(ItemsParam itemsParam)throws Exception;

	public Integer updateItemsParam(ItemsParam itemsParam)throws Exception;


}
