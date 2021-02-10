package com.zy.service.impl;

import com.zy.enums.YesOrNo;
import com.zy.idworker.Sid;
import com.zy.mapper.UserAddressMapper;
import com.zy.pojo.UserAddress;
import com.zy.pojo.bo.AddressBO;
import com.zy.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired(required = false)
    private UserAddressMapper userAddressMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("isDelete", YesOrNo.NO.type);
        return userAddressMapper.getUserAddressListByMap(param);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) throws Exception {
        UserAddress userAddress = new UserAddress();
        //此方法可以将相同的属性字段的值进行克隆
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(Sid.nextShort());
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddress.setIsDelete(YesOrNo.NO.type);
        /*
        1.判断是否存在地址，没有则新增默认地址
        2.入库
         */
        List<UserAddress> list = this.queryAll(addressBO.getUserId());
        if (list == null || list.size() == 0) {
            userAddress.setIsDefault(YesOrNo.YES.type);
        }
        userAddress.setIsDefault(YesOrNo.NO.type);
        userAddressMapper.insertUserAddress(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) throws Exception {
        UserAddress userAddress = new UserAddress();
        //此方法可以将相同的属性字段的值进行克隆
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressBO.getAddressId());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateUserAddress(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) throws Exception {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddress.setIsDelete(YesOrNo.YES.type);
        userAddressMapper.updateUserAddress(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("isDefault",YesOrNo.YES.type);
        //1.查询出原来的默认地址并修改成非默认
        List<UserAddress> list = userAddressMapper.getUserAddressListByMap(param);
        list.stream().forEach(node->{
            if(node.getIsDefault() == YesOrNo.YES.type){
                node.setIsDefault(YesOrNo.NO.type);
                try {
                    userAddressMapper.updateUserAddress(node);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //2.修改此为默认
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateUserAddress(userAddress);

    }
}
