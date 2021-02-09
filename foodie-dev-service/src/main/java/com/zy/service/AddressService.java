package com.zy.service;

import com.zy.pojo.UserAddress;
import com.zy.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id查询用户收获地址列表
     * @param userId
     * @return
     * @throws Exception
     */
    List<UserAddress> queryAll(String userId) throws Exception;

    /**
     * 新增地址
     * @param addressBO
     * @throws Exception
     */
    void addNewUserAddress(AddressBO addressBO) throws Exception;

    /**
     * 修改用户地址
     * @param addressBO
     * @throws Exception
     */
    void updateUserAddress(AddressBO addressBO) throws Exception;
}
