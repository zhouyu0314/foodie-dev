package com.zy.mapper;
import com.zy.entity.ItemsSpec;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemsSpecMapper {

	public ItemsSpec getItemsSpecById(@Param(value = "id") Long id)throws Exception;

	public List<ItemsSpec>	getItemsSpecListByMap(Map<String, Object> param)throws Exception;

	public Integer getItemsSpecCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItemsSpec(ItemsSpec itemsSpec)throws Exception;

	public Integer updateItemsSpec(ItemsSpec itemsSpec)throws Exception;


}
