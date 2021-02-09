package com.zy.controller;

import com.zy.pojo.bo.ShopcartBO;
import com.zy.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口", tags = {"购物车相关接口api"})
@RestController
@RequestMapping("/shopcart")
public class ShopCartController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        // TODO: 2021/2/9  前端用户在登录的情况下添加商品到购物车，会同时在后端同步购物车到redis
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public IMOOCJSONResult del(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                               @ApiParam(name = "specId", value = "规格id", required = true) @RequestParam String specId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(specId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空！");
        }

        // TODO: 2021/2/9  用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品
        return IMOOCJSONResult.ok();
    }
}
