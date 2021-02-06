package com.zy.service.impl;

import com.zy.entity.Users;
import com.zy.enums.Sex;
import com.zy.mapper.UsersMapper;
import com.zy.service.UsersService;
import com.zy.utils.DateUtil;
import com.zy.utils.IdWorker;
import com.zy.utils.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UsersServiceImpl implements UsersService {
    private static final String HEAD = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-71dcef82c8afb85dacd42a995f64f1b5_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1615088572&t=0e5f5005b1ec45623072dfc5f78c184a";

    @Autowired(required = false)
    private UsersMapper usersMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean usernameIsExist(String username) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        List<Users> users = this.findUserByUsername(username);
        if (users == null || users.size() == 0) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users register(String username, String pwd) throws Exception {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(Md5.EncoderByMd5(pwd));
        user.setCreatedTime(new Date());
        user.setBirthday(DateUtil.parse("1900-01-01 00:00", DateUtil.PATTERN_YYYY_MM_DDHHMM));
        user.setId(IdWorker.getId());
        user.setSex(Sex.SECRET.type);
        user.setUpdatedTime(new Date());
        user.setFace(HEAD);
        usersMapper.insertUsers(user);
        user = this.setNull(user);
        return user;
    }

    @Override
    public Users login(String username, String pwd) throws Exception {
        List<Users> users = this.findUserByUsername(username);
        if (users == null || users.size() == 0 || !Md5.checkpassword(pwd, users.get(0).getPassword())) {
            throw new Exception("用户名或密码错误！");
        }
        Users result = users.get(0);
        result = this.setNull(result);

        return result;
    }


    //*********************************************************
    private List<Users> findUserByUsername(String username)throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        return  usersMapper.getUsersListByMap(param);

    }
    private Users setNull(Users result){
        result.setPassword(null);
        result.setBirthday(null);
        result.setCreatedTime(null);
        result.setUpdatedTime(null);
        result.setMobile(null);
        result.setRealname(null);
        return result;
    }
}
