package com.zy.controller;

import com.zy.enums.PayMethod;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);


    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {
        try {
            //1.创建订单
            //2.创建订单以后，一处购物车中已结算（已提交）的商品
            //3.向支付中心发送当前订单，用于保存支付中心的订单数据

            if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                    && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
                return IMOOCJSONResult.errorMsg("支付方式不支持！");
            }
            System.out.println(submitOrderBO);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

}
