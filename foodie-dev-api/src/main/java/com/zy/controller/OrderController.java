package com.zy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.enums.OrderStatusEnum;
import com.zy.enums.PayMethod;
import com.zy.pojo.OrderStatus;
import com.zy.pojo.bo.ShopcartBO;
import com.zy.pojo.bo.SubmitOrderBO;
import com.zy.pojo.vo.MerchantOrdersVO;
import com.zy.pojo.vo.OrderVO;
import com.zy.resource.Urls;
import com.zy.service.OrderService;
import com.zy.utils.CookieUtils;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("/orders")
public class OrderController extends BaseController {
    @Autowired
    Urls urls;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        try {

            if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                    && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
                return IMOOCJSONResult.errorMsg("支付方式不支持！");
            }
            String redisResult = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
            if (StringUtils.isBlank(redisResult)) {
                return IMOOCJSONResult.errorMsg("购物车数据错误！");
            }
            List<ShopcartBO> shopCartList = JSONObject.parseArray(redisResult, ShopcartBO.class);
            //1.创建订单
            OrderVO orderVO = orderService.createOrder(shopCartList,submitOrderBO);
            String orderId = orderVO.getOrderId();
            List<ShopcartBO> toBeRemovedShopcastList = orderVO.getToBeRemovedShopcastList();
            MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
            merchantOrdersVO.setReturnUrl(urls.getPayReturnUrl());
            // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
            /**
             * 1001
             * 2002 -> 用户购买
             * 3003 -> 用户购买
             * 4004
             */
            //整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
            shopCartList.removeAll(toBeRemovedShopcastList);
            redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JSON.toJSONString(shopCartList));
            CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JSON.toJSONString(shopCartList), true);//先注销 方便支付测试

            //3.向支付中心发送当前订单，用于保存支付中心的订单数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("imoocUserId", "imooc");
            headers.add("password", "imooc");

            HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,headers);
            //调用支付中心，支付中心会保存当前订单的信息然后返回200
            ResponseEntity<IMOOCJSONResult> result = restTemplate.postForEntity(urls.getPaymentUrl(), entity, IMOOCJSONResult.class);
            IMOOCJSONResult paymentResult = result.getBody();
            if (paymentResult.getStatus() != 200) {
                return IMOOCJSONResult.errorMsg("支付中心顶大你创建失败，请练习管理员！");
            }
            return IMOOCJSONResult.ok(orderId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }
    }

    @ApiOperation(value = "支付中心调用此接口，通知支付成功", notes = "支付中心调用此接口，通知支付成功", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        try {
            orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
            return HttpStatus.OK.value();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    @ApiOperation(value = "轮询查看当前订单的状态", notes = "轮询查看当前订单的状态", httpMethod = "POST")
    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo( String orderId) {
        try {
            OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
            return IMOOCJSONResult.ok(orderStatus);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }
    }
}
