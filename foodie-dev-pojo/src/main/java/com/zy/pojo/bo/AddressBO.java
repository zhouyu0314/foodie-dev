package com.zy.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户新增或修改地址的BO
 */
@ApiModel(value = "地址对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class AddressBO implements Serializable {
    @ApiModelProperty(value = "地址id", name = "addressId", example = "190825CG3AA14Y3C", required = true)
    private String addressId;
    @ApiModelProperty(value = "用户id", name = "userId", example = "190825CG3AA14Y3C", required = true)
    private String userId;
    @ApiModelProperty(value = "收货人", name = "receiver", example = "jack", required = true)
    private String receiver;
    @ApiModelProperty(value = "手机号", name = "mobile", example = "13333333333", required = true)
    private String mobile;
    @ApiModelProperty(value = "省", name = "province", example = "北京", required = true)
    private String province;
    @ApiModelProperty(value = "市", name = "city", example = "北京", required = true)
    private String city;
    @ApiModelProperty(value = "区", name = "district", example = "东城区", required = true)
    private String district;
    @ApiModelProperty(value = "详细地址", name = "detail", example = "XX小区xxx", required = true)
    private String detail;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
