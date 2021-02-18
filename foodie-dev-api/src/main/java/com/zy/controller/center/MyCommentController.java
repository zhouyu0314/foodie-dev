package com.zy.controller.center;

import com.zy.controller.BaseController;
import com.zy.enums.YesOrNo;
import com.zy.pojo.OrderItems;
import com.zy.pojo.Orders;
import com.zy.pojo.bo.center.OrderItemsCommentBO;
import com.zy.service.center.MyCommentService;
import com.zy.utils.IMOOCJSONResult;
import com.zy.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关的api接口"})
@RestController
@RequestMapping("/mycomments")
public class MyCommentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCommentController.class);
    @Autowired
    private MyCommentService myCommentService;

    @ApiOperation(value = "查询可评价的商品列表", notes = "查询可评价的商品列表", httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                   @ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId) {
        try {
            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("未查询到此用户信息！");
            }

            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("用户ID不能为空！");
            }

            //判断用户和订单是否关联
            IMOOCJSONResult result = this.checkOrder(userId, orderId);
            if (result.getStatus() != HttpStatus.OK.value()) {
                return result;
            }
            Orders myOrder = (Orders) result.getData();

            //判断是否评价过
            if (myOrder.getIsComment() == YesOrNo.YES.type) {
                return IMOOCJSONResult.errorMsg("该订单已评价！");
            }

            List<OrderItems> list = myCommentService.queryPendingComment(orderId);
            return IMOOCJSONResult.ok(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("查询订单列表异常！");
        }
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                    @ApiParam(name = "orderId", value = "订单id", required = true) @RequestParam String orderId,
                                    @RequestBody List<OrderItemsCommentBO> commentList) {
        try {
            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("未查询到此用户信息！");
            }

            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("用户ID不能为空！");
            }

            //判断是否有评价内容
            if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
                return IMOOCJSONResult.errorMsg("评论内容不能为空！");
            }

            //判断用户和订单是否关联
            IMOOCJSONResult result = this.checkOrder(userId, orderId);
            if (result.getStatus() != HttpStatus.OK.value()) {
                return result;
            }
            myCommentService.saveComments(orderId, userId, commentList);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("保存评论列表异常！");
        }
    }


    @ApiOperation(value = "查询我的评价", notes = "查询我的评价", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                 @ApiParam(name = "page", value = "查询下一页的第几页", required = false, defaultValue = "1") @RequestParam Integer page,
                                 @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, defaultValue = "10") @RequestParam Integer pageSize) {
        try {
            if (StringUtils.isBlank(userId)) {
                return IMOOCJSONResult.errorMsg("未查询到此用户信息！");
            }
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = COMMON_PAGE_SIZE;
            }
            PagedGridResult result = myCommentService.queryMyComments(userId, page, pageSize);
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("查询订单列表异常！");
        }
    }
}
