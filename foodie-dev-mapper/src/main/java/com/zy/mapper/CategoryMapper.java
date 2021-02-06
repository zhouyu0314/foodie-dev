package com.zy.mapper;
import com.zy.entity.Category;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

	public Category getCategoryById(@Param(value = "id") Long id)throws Exception;

	public List<Category>	getCategoryListByMap(Map<String, Object> param)throws Exception;

	public Integer getCategoryCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertCategory(Category category)throws Exception;

	public Integer updateCategory(Category category)throws Exception;


}
