package com.zy.service.center;

import com.zy.pojo.Users;
import com.zy.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据用户i查询用户信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    Users queryUserInfo(String userId) throws Exception;

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     * @return
     * @throws Exception
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO) throws Exception;


    Users updateUserFace(String userId, String faceUrl) throws Exception;
}
