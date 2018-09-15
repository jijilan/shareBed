package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxShareOrders;
import com.sharebedapp.jijl.model.sub.WxShareOrdersInfo;
import com.sharebedapp.jijl.model.sub.WxShareOrdersSub;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component("wxShareOrdersMapper")
public interface WxShareOrdersMapper extends BaseMapper<WxShareOrders,String> {

    List<Map<String,Object>> getShareOrderseList(@Param("shareOrderId") String shareOrderId,
                                                 @Param("userPhone") String userPhone,
                                                 @Param("categoryId") String categoryId,
                                                 @Param("hospitalName")String hospitalName);

    Map<String,Object> getShareOrderse(String shareOrderId);

    List<WxShareOrdersInfo> getUserShareOrdersList(@Param("userId")String userId,
                                                   @Param("cTime")String cTime);

    Map<String,Object> getUserTime(String userId);

    int getShareOrderCount(@Param("categoryId") String categoryId);


    int getTodayAccount(@Param("categoryId")String categoryId);

    int getMonthAccount(@Param("categoryId")String categoryId);

    List<Map<String,Object>> getSevenOrderList();

    List<Map<String,Object>> getMonthOrderList();

    WxShareOrders getByoutTradeNo(@Param("outTradeNo")String outTradeNo);

    WxShareOrdersSub getLastOrders(@Param("userId")String userId);

    WxShareOrdersSub getFrontShareOrderse(@Param("shareOrderId")String shareOrderId);


    WxShareOrders selectByOutTradeNo(String outTradeNo);

    List<WxShareOrders> selectByEquipmentId(@Param("equipmentId") String equipmentId,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime,
                                      @Param("hospitalId") String hospitalId);

    Map<String,String> getShareOrdersEqNumber(@Param("equipmentNumber")String equipmentNumber);

    List<WxShareOrders> getByUserIdJin(String userId);

    List<WxShareOrders> getByUserIdWei(String userId);

    List<WxShareOrders> getByUserId(String userId);


    List<WxShareOrdersInfo> getUserShareOrdersTimeList(@Param("userId") String userId);

    List<WxShareOrders> selectByCondition(@Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("hospitalId") String hospitalId);

    BigDecimal calculateShareRevenue();
}