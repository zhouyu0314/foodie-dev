package com.zy.mapper;
import com.zy.entity.PdmanDbVersion;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PdmanDbVersionMapper {

	public PdmanDbVersion getPdmanDbVersionById(@Param(value = "id") Long id)throws Exception;

	public List<PdmanDbVersion>	getPdmanDbVersionListByMap(Map<String, Object> param)throws Exception;

	public Integer getPdmanDbVersionCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertPdmanDbVersion(PdmanDbVersion pdmanDbVersion)throws Exception;

	public Integer updatePdmanDbVersion(PdmanDbVersion pdmanDbVersion)throws Exception;


}
