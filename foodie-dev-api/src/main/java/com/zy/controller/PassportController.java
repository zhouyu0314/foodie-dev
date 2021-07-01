package com.zy.controller;

import com.zy.pojo.Users;
import com.zy.pojo.bo.ShopcartBO;
import com.zy.pojo.bo.UserBO;
import com.zy.pojo.vo.UsersVO;
import com.zy.resource.FileUpload;
import com.zy.service.UsersService;
import com.zy.utils.CookieUtils;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.JsonUtils;
import com.zy.utils.RedisOperator;
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
import java.util.ArrayList;
import java.util.List;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PassportController.class);
    @Autowired
    private UsersService usersService;

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private RedisOperator redisOperator;



    @ApiOperation(value = "查询用户名是否存在", notes = "存在返回true；不存在返回false", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(
            @ApiParam(name = "username", value = "用户名", required = true)
            @RequestParam String username) {
        //判断用户名是否为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不可谓空！");
        }

        try {
            if (usersService.usernameIsExist(username)) {
                return IMOOCJSONResult.errorMsg("用户名已存在！");
            }

            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
            //实现用户的redis会话
            UsersVO usersVO = this.conventUsersVO(result);

            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);//是否加密
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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
            IMOOCJSONResult imoocjsonResult = usersService.login(username, password);

            Users result = (Users)imoocjsonResult.getData();

            result.setFace(fileUpload.getFtpHttpPath() + ":" + fileUpload.getFtpHttpPort() + result.getFace());
            //生成用户token，存入redis会话
            UsersVO usersVO = this.conventUsersVO(result);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);//是否加密
            //同步购物车数据
            this.synchShopcartData(result.getId(), request, response);
            return IMOOCJSONResult.ok(usersVO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg(e.getMessage());
        }

    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    private void synchShopcartData(String userId, HttpServletRequest request,
                                   HttpServletResponse response) {

        /**
         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
         *                 如果cookie中的购物车不为空，此时直接放入redis中
         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         *                 如果cookie中的购物车不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖redis中（参考京东）
         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        // 从redis中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // 从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopcartStrCookie)) {

                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中
                 */

                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // 定义一个待删除list
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }

                    }
                }

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);

                // 合并两个list
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
            } else {
                // redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }

        }
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request,
                                  HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "user");

        //分布式会话中需要清除用户数据
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        return IMOOCJSONResult.ok();
    }

}
