package com.zy.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 需要注册到WebMvcConfig
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTokenInterceptor.class);
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    /**
     * 拦截请求，调用controller之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String UserId = request.getHeader("headerUserId");
        String UserToken = request.getHeader("headerUserToken");
        if (StringUtils.isBlank(UserId) || StringUtils.isBlank(UserToken)) {
            this.returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录......"));
            return false;
        }

        String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + UserId);
        if (StringUtils.isBlank(uniqueToken)) {
            this.returnErrorResponse(response,IMOOCJSONResult.errorMsg("账号在异地登录，请重新登录......"));
            return false;
        }

        if (!uniqueToken.equals(UserToken)) {
            this.returnErrorResponse(response,IMOOCJSONResult.errorMsg("请登录......"));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, IMOOCJSONResult imoocjsonResult){

        OutputStream os = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            os = response.getOutputStream();
            os.write(JSON.toJSONString(imoocjsonResult).getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os !=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOGGER.warn("OutputStream未关闭！\t" + e.getMessage());
                }
            }

        }
    }

    /**
     * 请求访问controller之后渲染视图之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
