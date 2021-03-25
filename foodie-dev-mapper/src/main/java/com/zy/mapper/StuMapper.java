package com.zy.mapper;

import com.zy.pojo.Stu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StuMapper {
    public Stu getStuById(@Param(value = "id") Integer id)throws Exception;

    public List<Stu> getStuListByMap(Map<String,Object> param)throws Exception;

    public Integer getStuCountByMap(Map<String,Object> param)throws Exception;

    public Integer insertStu(Stu stu)throws Exception;

    public Integer updateStu(Stu stu)throws Exception;
}
