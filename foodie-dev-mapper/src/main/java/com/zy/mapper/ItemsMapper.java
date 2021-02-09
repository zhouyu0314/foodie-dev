package com.zy.mapper;

import com.zy.pojo.Items;
import com.zy.pojo.vo.SearchItemsVO;
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

	List<SearchItemsVO> searchItems(Map<String,Object> param)throws Exception;

	List<SearchItemsVO> searchItemsByThirdCat(Map<String,Object> param)throws Exception;

}
