package com.zy.controller;

import com.zy.pojo.UserAddress;
import com.zy.pojo.bo.AddressBO;
import com.zy.service.AddressService;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {
        try {
            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }
            List<UserAddress> list = addressService.queryAll(userId);
            return IMOOCJSONResult.ok(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO) {
        try {
            IMOOCJSONResult result = checkAddress(addressBO);
            if (result.getStatus() != 200) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }
            addressService.addNewUserAddress(addressBO);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO) {
        try {
            if (StringUtils.isBlank(addressBO.getAddressId())) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }

            IMOOCJSONResult result = checkAddress(addressBO);
            if (result.getStatus() != 200) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }
            addressService.updateUserAddress(addressBO);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }


    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                  @ApiParam(name = "addressId", value = "addressId", required = true) @RequestParam String addressId) {
        try {
            if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }
            addressService.deleteUserAddress(userId, addressId);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
            @ApiParam(name = "addressId", value = "addressId", required = true) @RequestParam String addressId) {

        try {
            if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
                return IMOOCJSONResult.errorMsg("参数错误！");
            }
            addressService.updateUserAddressToBeDefault(userId, addressId);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

//******************************************************************************************

    private IMOOCJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }
        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }
        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }
        return IMOOCJSONResult.ok();
    }

}
