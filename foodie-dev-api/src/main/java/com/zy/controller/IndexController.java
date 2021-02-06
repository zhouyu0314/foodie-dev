package com.zy.controller;

import com.zy.config.IMOOCJSONResult;
import com.zy.entity.Carousel;
import com.zy.enums.YesOrNo;
import com.zy.service.CarouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private CarouseService carouseService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        try {
            List<Carousel> carousels = carouseService.queryAll(YesOrNo.YES.type);
            return IMOOCJSONResult.ok(carousels);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }
}
