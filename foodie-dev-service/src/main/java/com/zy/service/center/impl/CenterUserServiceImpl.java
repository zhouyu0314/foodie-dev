package com.zy.service.center.impl;

import com.zy.mapper.UsersMapper;
import com.zy.pojo.Users;
import com.zy.pojo.bo.center.CenterUserBO;
import com.zy.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {
    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) throws Exception {
        Users user = usersMapper.getUsersById(userId);
        user.setPassword(null);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) throws Exception {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO,user);
        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateUsers(user);
        return this.queryUserInfo(userId);
    }
}
