package com.zy.pojo.vo;

import com.zy.pojo.bo.ShopcartBO;

import java.io.Serializable;
import java.util.List;

public class OrderVO implements Serializable {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopcartBO> toBeRemovedShopcastList;

    public List<ShopcartBO> getToBeRemovedShopcastList() {
        return toBeRemovedShopcastList;
    }

    public void setToBeRemovedShopcastList(List<ShopcartBO> toBeRemovedShopcastList) {
        this.toBeRemovedShopcastList = toBeRemovedShopcastList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}