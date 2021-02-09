package com.zy.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "购物车对象VO", description = "后端生成返回给前端的对象模型")
public class ShopcartVO implements Serializable {
    @ApiModelProperty(value = "商品id", name = "itemId", example = "cake-1001", required = true)
    private String itemId;
    @ApiModelProperty(value = "商品图片地址", name = "itemImgUrl", example = "http://122.152.205.72:88/foodie/cake-1001/img1.png", required = true)
    private String itemImgUrl;
    @ApiModelProperty(value = "商品名称", name = "itemName", example = "【天天吃货】彩虹马卡龙 下午茶 美眉最爱", required = true)
    private String itemName;
    @ApiModelProperty(value = "规格id", name = "itemName", example = "1", required = true)
    private String specId;
    @ApiModelProperty(value = "规格名称", name = "specName", example = "原味", required = true)
    private String specName;
    @ApiModelProperty(value = "优惠价格", name = "priceDiscount", example = "1", required = true)
    private String priceDiscount;
    @ApiModelProperty(value = "正常价格", name = "priceNormal", example = "1", required = true)
    private String priceNormal;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(String priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getPriceNormal() {
        return priceNormal;
    }

    public void setPriceNormal(String priceNormal) {
        this.priceNormal = priceNormal;
    }

}
