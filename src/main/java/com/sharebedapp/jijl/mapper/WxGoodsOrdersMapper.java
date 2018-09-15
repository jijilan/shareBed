package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxGoodsOrders;
import com.sharebedapp.jijl.model.wrap.GoodsOrdersInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component("wxGoodsOrdersMapper")
public interface WxGoodsOrdersMapper extends BaseMapper<WxGoodsOrders,String>{

    List<Map<String,Object>> getGoodsOrdersList(@Param("goodsOrdersId") String goodsOrdersId,
                                                @Param("userPhone") String userPhone,
                                                @Param("categoryId") String categoryId);

    Map<String,Object> getGoodsOrders(String goodsOrdersId);

    int getGoodsOrderCount(@Param("categoryId")String categoryId);

    List<Map<String,Object>> getSevenOrderList();

    List<Map<String,Object>> getMonthOrderList();

    WxGoodsOrders selectByOutTradeNo(String outTradeNo);

    List<GoodsOrdersInfo> selectGoodsOrdersInfo(@Param("orderStatus") Integer orderStatus,
                                                @Param("userId") String userId);
}