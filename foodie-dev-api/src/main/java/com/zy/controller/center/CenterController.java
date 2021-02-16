package com.zy.controller.center;

import com.zy.pojo.Users;
import com.zy.service.center.CenterUserService;
import com.zy.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "center - 用户中心", tags = {"用户中心相关的api接口"})
@RestController
@RequestMapping("/center")
public class CenterController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(CenterController.class);
    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {
        try {
            Users users = centerUserService.queryUserInfo(userId);
            return IMOOCJSONResult.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("用户信息查询异常");
        }
    }
}
