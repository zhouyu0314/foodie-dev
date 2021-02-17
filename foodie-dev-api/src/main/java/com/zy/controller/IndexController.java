package com.zy.controller;

import com.zy.pojo.Carousel;
import com.zy.pojo.Category;
import com.zy.enums.YesOrNo;
import com.zy.pojo.vo.CategoryVO;
import com.zy.pojo.vo.NewItemsVO;
import com.zy.service.CarouseService;
import com.zy.service.CategoryService;
import com.zy.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private CarouseService carouseService;

    @Autowired
    private CategoryService categoryService;

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

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() {
        try {
            List<Category> cats = categoryService.queryAllRootLevelCat();
            return IMOOCJSONResult.ok(cats);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }


    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult cats(
            @ApiParam(name="rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId) {
        try {
            if (rootCatId == null) {
                return IMOOCJSONResult.errorMsg("分类不存在！");
            }
            List<CategoryVO> subCats = categoryService.getSubCatList(rootCatId);
            return IMOOCJSONResult.ok(subCats);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(
            @ApiParam(name="rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId) {
        try {
            if (rootCatId == null) {
                return IMOOCJSONResult.errorMsg("分类不存在！");
            }
            List<NewItemsVO> sixNewItems = categoryService.getSixNewItemsLazy(rootCatId);
            return IMOOCJSONResult.ok(sixNewItems);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

}
