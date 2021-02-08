package com.zy.mapper;
import com.zy.pojo.ItemsComments;
import com.zy.pojo.vo.ItemCommentVO;
import com.zy.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsCommentsMapper {

	public ItemsComments getItemsCommentsById(@Param(value = "id") Long id)throws Exception;

	public List<ItemsComments>	getItemsCommentsListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsCommentsCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsComments(ItemsComments itemsComments)throws Exception;

	public Integer updateItemsComments(ItemsComments itemsComments)throws Exception;

	List<ItemCommentVO> queryItemComments(Map<String,Object> param)throws Exception;

	List<SearchItemsVO> searchItems(Map<String,Object> param)throws Exception;


}
