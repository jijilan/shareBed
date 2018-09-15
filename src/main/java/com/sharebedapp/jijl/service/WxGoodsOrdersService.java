package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxGoodsOrders;
import com.sharebedapp.jijl.result.ResultView;
import com.github.pagehelper.PageInfo;

public interface WxGoodsOrdersService {
    ResultView getGoodsOrdersList(String goodsOrdersId, String userPhone, String categoryId, Integer pageNo, Integer pageSize);

    ResultView getGoodsOrders(String goodsOrdersId);

    int getGoodsOrderCount(String categoryId);

    String createGoodsOrders(String hospitalId, String categoryId, Integer totalNum, String agentId, String userId);

    PageInfo getOrdersList(Integer pageNo, Integer pageSize, Integer orderStatus, String userId);

    void callback(String outTradeNo, Integer payType);

    int updateGoodsOrdersStatus(String goodsOrdersId, Integer ordersStatus);

    WxGoodsOrders selectByOutTradeNo(String outTradeNo);
}
