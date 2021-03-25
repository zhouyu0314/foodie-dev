package com.zy.config;

import com.zy.controller.interceptor.UserTokenInterceptor;
import com.zy.resource.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private FileUpload fileUpload;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    //实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")  // 映射swagger2
                //映射本地静态资源
                .addResourceLocations(fileUpload.getResource());
    }

    /**
     * 注册拦截器,将拦截器加入IOC然后注册
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userTokenInterceptor())
//                .addPathPatterns("/hello")
//                .addPathPatterns("/shopcart/add")
//                .addPathPatterns("/shopcart/del")
//                .addPathPatterns("/address/list")
//                .addPathPatterns("/address/add")
//                .addPathPatterns("/address/update")
//                .addPathPatterns("/address/setDefalut")
//                .addPathPatterns("/address/delete")
//                .addPathPatterns("/orders/*")
//                .addPathPatterns("/center/*")
//                .addPathPatterns("/userInfo/*")
//                .addPathPatterns("/myorders/*")
//                .addPathPatterns("/mycomments/*")
//                .excludePathPatterns("/myorders/deliver")
//                .excludePathPatterns("/orders/notifyMerchantOrderPaid");
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
}