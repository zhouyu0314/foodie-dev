package com.zy.mapper;
import com.zy.entity.ItemsImg;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemsImgMapper {

	public ItemsImg getItemsImgById(@Param(value = "id") Long id)throws Exception;

	public List<ItemsImg>	getItemsImgListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsImgCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsImg(ItemsImg itemsImg)throws Exception;

	public Integer updateItemsImg(ItemsImg itemsImg)throws Exception;


}
