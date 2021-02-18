package com.zy.controller;


import com.zy.pojo.Orders;
import com.zy.service.center.MyOrdersService;
import com.zy.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://153.36.170.3:38089/payment/createMerchantOrder";        // produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    String payReturnUrl = "http://153.36.170.3:38088/orders/notifyMerchantOrderPaid";


    // 用户上传头像的位置(写在配置文件里)
//    public static final String IMAGE_USER_FACE_LOCATION ="C:"+ File.separator + "workspaces" +
//            File.separator + "images" +
//            File.separator + "foodie" +
//            File.separator + "faces";
//    public static final String IMAGE_USER_FACE_LOCATION = "C:/workspaces/images/foodie/faces";

//
    @Autowired
    private MyOrdersService myOrdersService;
    /**
     * 用于验证用户和订单是否有关联关系，避免用户恶意调用
     *
     * @return
     */
    protected IMOOCJSONResult checkOrder(String userId, String orderId) throws Exception {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return IMOOCJSONResult.errorMsg("订单不存在！");
        }
        return IMOOCJSONResult.ok(order);
    }
}
