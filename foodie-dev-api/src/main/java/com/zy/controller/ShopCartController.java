package com.zy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zy.pojo.bo.ShopcartBO;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "购物车接口", tags = {"购物车相关接口api"})
@RestController
@RequestMapping("/shopcart")
public class ShopCartController extends BaseController {
    //asdasdasda
    @Autowired
    RedisOperator redisOperator;


    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        //前端用户在登录的情况下添加商品到购物车，会同时在后端同步购物车到redis
        // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
        List<ShopcartBO> shopcartList = null;
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (!StringUtils.isBlank(shopCartJson)) {
            //redis中有购物车数据
            shopcartList = JSONObject.parseArray(shopCartJson, ShopcartBO.class);
            //遍历查询有没有本次添加的商品，如果有则修改其数量
            boolean isHaving = false;
            for (ShopcartBO bo : shopcartList) {
                if (shopcartBO.getSpecId().equals(bo.getSpecId())) {
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                    break;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            shopcartList = new ArrayList<>();
            shopcartList.add(shopcartBO);
        }
        //+redis
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JSONObject.toJSONString(shopcartList));
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                               @ApiParam(name = "itemSpecId", value = "规格id", required = true) @RequestParam String itemSpecId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空！");
        }

        // TODO: 2021/2/9  用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品
        List<ShopcartBO> shopcartList = null;
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (!StringUtils.isBlank(shopCartJson)) {
            //如果购物车中存在此商品
            shopcartList = JSONObject.parseArray(shopCartJson, ShopcartBO.class);
            for (ShopcartBO shopcart : shopcartList) {
                if (itemSpecId.equals(shopcart.getSpecId())) {
                    shopcartList.remove(shopcart);
                    break;
                }
            }
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JSON.toJSONString(shopcartList));
        }
        return IMOOCJSONResult.ok();
    }
}
