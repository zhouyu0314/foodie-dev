package com.zy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ItemsController {
     private static final Logger LOGGER =  LoggerFactory.getLogger(ItemsController.class);

    @GetMapping("/hello")
    public Object hello(){

        return "Hello World!";
    }



}
