package com.zy.mapper;
import com.zy.pojo.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserAddressMapper {

	public UserAddress getUserAddressById(@Param(value = "id") String id)throws Exception;

	public List<UserAddress>	getUserAddressListByMap(Map<String, Object> param)throws Exception;

	public Integer getUserAddressCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertUserAddress(UserAddress userAddress)throws Exception;

	public Integer updateUserAddress(UserAddress userAddress)throws Exception;


}
