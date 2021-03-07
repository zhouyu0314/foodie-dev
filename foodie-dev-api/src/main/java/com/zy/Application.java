package com.zy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//排除此类 因为需要先输入账号密码才能访问
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//开启定时任务
@EnableScheduling
//开启使用redis作为spring session
@EnableRedisHttpSession
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
