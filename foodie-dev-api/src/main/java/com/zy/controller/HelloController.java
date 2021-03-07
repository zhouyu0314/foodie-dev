package com.zy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@ApiIgnore//加了此注解之后，此类在swagger2 就不显示了
@RestController
@CrossOrigin
public class HelloController {
1
     private static final Logger LOGGER =  LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public Object hello(){
        LOGGER.debug("debug: hello");
        LOGGER.info("info: hello");//默认级别是info，若要修改级别，可在log4j.properties配置中修改
        LOGGER.warn("warn: hello");
        LOGGER.error("error hello");
        return "Hello World!";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo","new user");
        session.setMaxInactiveInterval(3600);
        return "ok!";
    }
}
