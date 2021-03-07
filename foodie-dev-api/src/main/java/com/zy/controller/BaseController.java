package com.zy.controller;


import com.zy.pojo.Orders;
import com.zy.pojo.Users;
import com.zy.pojo.vo.UsersVO;
import com.zy.service.center.MyOrdersService;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class BaseController {

    @Autowired
    private MyOrdersService myOrdersService;
    @Autowired
    private RedisOperator redisOperator;

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // 支付中心的调用地址
//    String paymentUrl = urls.getPaymentUrl();
    String paymentUrl;

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
//    String payReturnUrl = urls.getPayReturnUrl();
    String payReturnUrl;




    // 用户上传头像的位置(写在配置文件里)
//    public static final String IMAGE_USER_FACE_LOCATION ="C:"+ File.separator + "workspaces" +
//            File.separator + "images" +
//            File.separator + "foodie" +
//            File.separator + "faces";
//    public static final String IMAGE_USER_FACE_LOCATION = "C:/workspaces/images/foodie/faces";



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

    /**
     * 将用户信息存入redis
     * @param result
     * @return
     */
    protected UsersVO conventUsersVO(Users result) {
        //实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + result.getId(), uniqueToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }


}
