package com.zy.controller;

import com.zy.config.CookieUtils;
import com.zy.config.IMOOCJSONResult;
import com.zy.config.JsonUtils;
import com.zy.entity.Users;
import com.zy.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UsersService usersService;

    @ApiOperation(value = "查询用户名是否存在", notes = "存在返回true；不存在返回false", httpMethod = "GET")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
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


    @ApiOperation(value = "用户注册", notes = "入参Map 其中String username;password;confirmPassword必传", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult register(@RequestBody Map<String, Object> param,HttpServletRequest request,HttpServletResponse response) {

        try {
            if (StringUtils.isEmpty(param.get("username")) || StringUtils.isEmpty(param.get("password")) || StringUtils.isEmpty(param.get("confirmPassword"))) {
                return IMOOCJSONResult.errorMsg("缺少参数！");

            }
            String username = param.get("username").toString();
            String password = param.get("password").toString();
            String confirmPwd = param.get("confirmPassword").toString();

            if (usersService.usernameIsExist(username)) {
                return IMOOCJSONResult.errorMsg("用户名已经存在！");
            }
            //判断两次密码是否一致
            if (!password.equals(confirmPwd)) {
                return IMOOCJSONResult.errorMsg("两次密码不一致！");
            }

            Users result = usersService.register(username, password);
            CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(result),true);//是否加密
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg("服务异常！");
        }

    }

    @ApiOperation(value = "用户登录", notes = "入参Map 其中String username;password必传", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody Map<String, Object> param, HttpServletRequest request,
                                 HttpServletResponse response) {
        //判断用户名密码是否为空
        if (StringUtils.isEmpty(param.get("username")) || StringUtils.isEmpty(param.get("password"))) {
            return IMOOCJSONResult.errorMsg("账户或者密码不可为空");
        }
        try {
            String username = param.get("username").toString();
            String password = param.get("password").toString();
            Users result = usersService.login(username, password);
            CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(result),true);//是否加密

            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }

    }

}
