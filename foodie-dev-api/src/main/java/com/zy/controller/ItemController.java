package com.zy.controller;

import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;
import com.zy.pojo.vo.CommentLevelCountsVO;
import com.zy.pojo.vo.ItemInfoVO;
import com.zy.pojo.vo.ShopcartVO;
import com.zy.service.ItemService;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(@ApiParam(name = "itemId", value = "商品id", required = true) @PathVariable String itemId) {
        try {
            if (StringUtils.isBlank(itemId)) {
                return IMOOCJSONResult.errorMsg("商品不存在！");
            }
            Items item = itemService.queryItemById(itemId);
            List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
            List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
            List<ItemsParam> itemsParamList = itemService.queryItemsParam(itemId);
            ItemInfoVO itemInfoVO = new ItemInfoVO();
            itemInfoVO.setItem(item);
            itemInfoVO.setItemImgList(itemsImgList);
            itemInfoVO.setItemSpecList(itemsSpecList);
            if (itemsParamList == null || itemsParamList.size() == 0) {
                itemInfoVO.setItemParams(null);
            } else {
                itemInfoVO.setItemParams(itemsParamList.get(0));
            }
            return IMOOCJSONResult.ok(itemInfoVO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "查询评价等级", notes = "查询评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(@ApiParam(name = "itemId", value = "商品id", required = true) @RequestParam String itemId) {
        try {
            if (StringUtils.isBlank(itemId)) {
                return IMOOCJSONResult.errorMsg("商品不存在！");
            }
            CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
            return IMOOCJSONResult.ok(commentLevelCountsVO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public IMOOCJSONResult comments(@ApiParam(name = "itemId", value = "商品id", required = true) @RequestParam String itemId,
                                    @ApiParam(name = "level", value = "评价等级", required = false) @RequestParam Integer level,
                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false, defaultValue = "1") @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, defaultValue = "10") @RequestParam Integer pageSize) {
        try {
            if (StringUtils.isBlank(itemId)) {
                return IMOOCJSONResult.errorMsg("商品不存在！");
            }
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = COMMON_PAGE_SIZE;
            }
            PagedGridResult result = itemService.queryPagedComments(itemId, level, page, pageSize);
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }


    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public IMOOCJSONResult comments(@ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam String keywords,
                                    @ApiParam(name = "sort", value = "排序", required = false) @RequestParam String sort,
                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false, defaultValue = "1") @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, defaultValue = "20") @RequestParam Integer pageSize) {

        try {
            if (StringUtils.isBlank(keywords)) {
                return IMOOCJSONResult.errorMsg("关键字不能为空！");
            }
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = PAGE_SIZE;
            }
            PagedGridResult result = itemService.searchItems(keywords, sort, page, pageSize);
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }


    }

    @ApiOperation(value = "根据分类id搜索商品列表", notes = "根据分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public IMOOCJSONResult catItems(@ApiParam(name = "catId", value = "三级分类id", required = true) @RequestParam Integer catId,
                                    @ApiParam(name = "sort", value = "排序", required = false) @RequestParam String sort,
                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false, defaultValue = "1") @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, defaultValue = "20") @RequestParam Integer pageSize) {
        try {
            if (catId == null) {
                return IMOOCJSONResult.errorMsg("关键字不能为空！");
            }
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = PAGE_SIZE;
            }
            PagedGridResult result = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }


    //用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult refresh(@ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1002,1003") @RequestParam String itemSpecIds) {
        try {
            if (StringUtils.isBlank(itemSpecIds)) {
                return IMOOCJSONResult.ok();
            }
            List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
            return IMOOCJSONResult.ok(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("服务异常！");
        }
    }
}
