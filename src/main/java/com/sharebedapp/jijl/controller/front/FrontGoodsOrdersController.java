package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxGoodsOrdersService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jijl
 * @Date 2018/8/24 13:58
 */
@RestController
@RequestMapping("/front")
public class FrontGoodsOrdersController {

    @Autowired
    private WxGoodsOrdersService wxGoodsOrdersService;

    @PostMapping("/createGoodsOrders")
    public ResultView createGoodsOrders(HttpServletRequest request ,String hospitalId, String categoryId, Integer totalNum, String agentId) {
        if (StringUtils.isEmpty(hospitalId)) {
            return ResultView.error(ResultEnum.CODE_21);
        }
        if (StringUtils.isEmpty(categoryId)) {
            return ResultView.error(ResultEnum.CODE_24);
        }
        if (totalNum == null || totalNum <= 0) {
            return ResultView.error(ResultEnum.CODE_54);
        }
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        String outTradeNo = wxGoodsOrdersService.createGoodsOrders(hospitalId, categoryId, totalNum, agentId, userId);
        if (outTradeNo != null && !outTradeNo.substring(0,3).equalsIgnoreCase("GOU") ) {
            return ResultView.error(ResultEnum.CODE_59);
        }else if (outTradeNo != null && outTradeNo.substring(0,3).equalsIgnoreCase("GOU")) {

            return ResultView.ok(outTradeNo);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("getOrdersList")
    public ResultView getOrdersList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    HttpServletRequest request,Integer orderStatus) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        if (orderStatus != null && orderStatus != ResultStatus.ORDERS_STATUS_UNPAY && orderStatus != ResultStatus.ORDERS_STATUS_PAY  && orderStatus != ResultStatus.ORDERS_STATUS_CANCELED){
            orderStatus = null;
        }
        if (userId != null) {
            PageInfo pageInfo = wxGoodsOrdersService.getOrdersList(pageNo, pageSize, orderStatus, userId);
            return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo, "ordersList"));
        }
        return ResultView.error(ResultEnum.CODE_62);
    }

    @PostMapping("/cancelOrders")
    public ResultView cancelOrders(String goodsOrdersId){

        if (wxGoodsOrdersService.updateGoodsOrdersStatus(goodsOrdersId,ResultStatus.ORDERS_STATUS_CANCELED)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
