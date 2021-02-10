package com.zy.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用于创建订单的BO对象
 */
@ApiModel(value = "订单对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
public class SubmitOrderBO implements Serializable {
    @ApiModelProperty(value = "用户id", name = "userId", required = true)
    private String userId;
    @ApiModelProperty(value = "商品规格id", name = "itemSpecIds", required = true)
    private String itemSpecIds;
    @ApiModelProperty(value = "地址id", name = "addressId", required = true)
    private String addressId;
    @ApiModelProperty(value = "支付方式", name = "payMethod", required = true)
    private Integer payMethod;
    @ApiModelProperty(value = "买家留言", name = "leftMsg", required = true)
    private String leftMsg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemSpecIds() {
        return itemSpecIds;
    }

    public void setItemSpecIds(String itemSpecIds) {
        this.itemSpecIds = itemSpecIds;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getLeftMsg() {
        return leftMsg;
    }

    public void setLeftMsg(String leftMsg) {
        this.leftMsg = leftMsg;
    }

    @Override
    public String toString() {
        return "SubmitOrderBO{" +
                "userId='" + userId + '\'' +
                ", itemSpecIds='" + itemSpecIds + '\'' +
                ", addressId='" + addressId + '\'' +
                ", payMethod=" + payMethod +
                ", leftMsg='" + leftMsg + '\'' +
                '}';
    }
}
