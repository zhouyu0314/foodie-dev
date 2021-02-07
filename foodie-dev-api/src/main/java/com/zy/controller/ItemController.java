package com.zy.controller;

import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;
import com.zy.pojo.vo.ItemInfoVO;
import com.zy.service.ItemService;
import com.zy.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "商品接口",tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(@ApiParam(name="itemId",value = "商品id",required = true) @PathVariable String itemId) {
        try {
            if (StringUtils.isBlank(itemId)) {
                return IMOOCJSONResult.errorMsg("商品不存在！");
            }
            Items item = itemService.queryItemById(itemId);
            List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
            List<ItemsSpec> itemsSpecList = itemService.queryItemApecList(itemId);
            List<ItemsParam> itemsParamList = itemService.queryItemsParam(itemId);
            ItemInfoVO itemInfoVO = new ItemInfoVO();
            itemInfoVO.setItem(item);
            itemInfoVO.setItemImgList(itemsImgList);
            itemInfoVO.setItemSpecList(itemsSpecList);
            if (itemsParamList == null || itemsParamList.size()==0) {
                itemInfoVO.setItemParams(null);
            }else{
                itemInfoVO.setItemParams(itemsParamList.get(0));
            }
            return IMOOCJSONResult.ok(itemInfoVO);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }
}
