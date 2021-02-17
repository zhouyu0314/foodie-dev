package com.zy.controller.center;

import com.zy.controller.BaseController;
import com.zy.service.center.MyOrdersService;
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

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关的api接口"})
@RestController
@RequestMapping("/myorders")
public class MyOrdersController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyOrdersController.class);

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId,
                                 @ApiParam(name = "orderStatus", value = "订单状态", required = false) @RequestParam Integer orderStatus,
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
            PagedGridResult result = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
            return IMOOCJSONResult.ok(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("查询订单列表异常！");
        }
    }

    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
                return IMOOCJSONResult.errorMsg("订单ID不能为空");
            }
            myOrdersService.updateDeliverOrderStatus(orderId);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return IMOOCJSONResult.errorMsg("商家发货接口异常！");
        }
    }
}
