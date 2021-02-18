package com.zy.mapper;

import com.zy.pojo.ItemsComments;
import com.zy.pojo.vo.ItemCommentVO;
import com.zy.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsCommentsMapper {

	ItemsComments getItemsCommentsById(@Param(value = "id") String id)throws Exception;

	List<ItemsComments>	getItemsCommentsListByMap(Map<String, Object> param)throws Exception;

	Integer getItemsCommentsCountByMap(Map<String, Object> param)throws Exception;

	Integer insertItemsComment(ItemsComments itemsComments)throws Exception;

	/**
	 * 插入多条数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Integer insertItemsComments(Map<String,Object> param)throws Exception;

	Integer updateItemsComments(ItemsComments itemsComments)throws Exception;

	List<ItemCommentVO> queryItemComments(Map<String,Object> param)throws Exception;

	List<MyCommentVO> queryMyComments(Map<String,Object> param)throws Exception;



}
