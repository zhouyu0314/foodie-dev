package com.zy.controller;

import com.zy.pojo.Users;
import com.zy.pojo.bo.UserBO;
import com.zy.service.UsersService;
import com.zy.utils.CookieUtils;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(PassportController.class);
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "查询用户名是否存在", notes = "存在返回true；不存在返回false", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(
            @ApiParam(name="username",value = "用户名",required = true)
            @RequestParam String username) {
        //判断用户名是否为空
        if (StringUtils.isEmpty(username)) {
            return IMOOCJSONResult.errorMsg("用户名不可谓空！");
        }

        try {
            if (usersService.usernameIsExist(username)) {
                return IMOOCJSONResult.errorMsg("用户名已存在！");
            }

            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg("服务异常！");
        }

    }


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult register(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {

        try {
            String username = userBO.getUsername();
            String password = userBO.getPassword();
            String confirmPwd = userBO.getConfirmPassword();

            // 0. 判断用户名和密码必须不为空
            if (StringUtils.isBlank(username) ||
                    StringUtils.isBlank(password) ||
                    StringUtils.isBlank(confirmPwd)) {
                return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
            }

            if (usersService.usernameIsExist(username)) {
                return IMOOCJSONResult.errorMsg("用户名已经存在！");
            }
            //判断两次密码是否一致
            if (!password.equals(confirmPwd)) {
                return IMOOCJSONResult.errorMsg("两次密码不一致！");
            }

            Users result = usersService.register(username, password);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);//是否加密
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg("服务异常！");
        }

    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request,
                                 HttpServletResponse response) {
        try {
            String username = userBO.getUsername();
            String password = userBO.getPassword();
            //判断用户名密码是否为空
            if (StringUtils.isBlank(username) ||
                    StringUtils.isBlank(password)) {
                return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
            }
            Users result = usersService.login(username, password);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(result), true);//是否加密
            // TODO: 2021/2/9 生成用户token，存入redis会话
            // TODO: 2021/2/9 同步购物车数据
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }

    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request,
                                  HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");

        // TODO: 2021/2/9  用户退出登录，需要清空购物车
        // TODO: 2021/2/9  分布式会话中需要清除用户数据
        return IMOOCJSONResult.ok();
    }
}
