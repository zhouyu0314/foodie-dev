package com.zy.service;

import com.zy.pojo.Users;
import com.zy.utils.IMOOCJSONResult;

public interface UsersService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return存在 true  不存在false
     * @throws Exception
     */
    boolean usernameIsExist(String username)throws Exception;

    /**
     * 用户注册
     * @param username 用户名
     * @param pwd 密码
     * @throws Exception
     */
    Users register(String username,String pwd)throws Exception;

    /**
     * 用户登录
     * @param username 用户名
     * @param pwd 密码
     * @throws Exception
     */
    IMOOCJSONResult login(String username, String pwd)throws Exception;

}
