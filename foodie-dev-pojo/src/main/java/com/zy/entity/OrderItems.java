package com.zy.entity;
import java.io.Serializable;
import java.util.Date;
/***
*   订单商品关联表 
*/
public class OrderItems implements Serializable {
    //主键id
    private String id;
    //归属订单id
    private String orderId;
    //商品id
    private String itemId;
    //商品图片
    private String itemImg;
    //商品名称
    private String itemName;
    //规格id
    private String itemSpecId;
    //规格名称
    private String itemSpecName;
    //成交价格
    private Integer price;
    //购买数量
    private Integer buyCounts;
    //get set 方法
    public void setId (String  id){
        this.id=id;
    }
    public  String getId(){
        return this.id;
    }
    public void setOrderId (String  orderId){
        this.orderId=orderId;
    }
    public  String getOrderId(){
        return this.orderId;
    }
    public void setItemId (String  itemId){
        this.itemId=itemId;
    }
    public  String getItemId(){
        return this.itemId;
    }
    public void setItemImg (String  itemImg){
        this.itemImg=itemImg;
    }
    public  String getItemImg(){
        return this.itemImg;
    }
    public void setItemName (String  itemName){
        this.itemName=itemName;
    }
    public  String getItemName(){
        return this.itemName;
    }
    public void setItemSpecId (String  itemSpecId){
        this.itemSpecId=itemSpecId;
    }
    public  String getItemSpecId(){
        return this.itemSpecId;
    }
    public void setItemSpecName (String  itemSpecName){
        this.itemSpecName=itemSpecName;
    }
    public  String getItemSpecName(){
        return this.itemSpecName;
    }
    public void setPrice (Integer  price){
        this.price=price;
    }
    public  Integer getPrice(){
        return this.price;
    }
    public void setBuyCounts (Integer  buyCounts){
        this.buyCounts=buyCounts;
    }
    public  Integer getBuyCounts(){
        return this.buyCounts;
    }
}
