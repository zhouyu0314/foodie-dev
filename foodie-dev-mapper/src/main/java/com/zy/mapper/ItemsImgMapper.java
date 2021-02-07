package com.zy.mapper;
import com.zy.pojo.ItemsImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsImgMapper {

	public ItemsImg getItemsImgById(@Param(value = "id") String id)throws Exception;

	public List<ItemsImg>	getItemsImgListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsImgCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsImg(ItemsImg itemsImg)throws Exception;

	public Integer updateItemsImg(ItemsImg itemsImg)throws Exception;


}
