package com.zy.mapper;
import com.zy.entity.ItemsComments;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemsCommentsMapper {

	public ItemsComments getItemsCommentsById(@Param(value = "id") Long id)throws Exception;

	public List<ItemsComments>	getItemsCommentsListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsCommentsCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsComments(ItemsComments itemsComments)throws Exception;

	public Integer updateItemsComments(ItemsComments itemsComments)throws Exception;


}
