package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxShareOrders;
import com.sharebedapp.jijl.model.sub.WxShareOrdersSub;
import com.sharebedapp.jijl.result.ResultView;

import java.util.List;
import java.util.Map;

public interface WxShareOrdersService {
    ResultView getShareOrderseList(String shareOrderId, String userPhone, String categoryId,String hospitalName, Integer pageNo, Integer pageSize);

    ResultView getShareOrderse(String shareOrderId);

    ResultView getUserShareOrdersList(String userId, Integer pageNo, Integer pageSize);

    ResultView getUserTime(String userId);

    int getShareOrderCount(String categoryId);


    int getTodayAccount(String categoryId);

    int getMonthAccount(String categoryId);


    ResultView isOutTime(String equipmentNumber, Long ctime, String outTradeNo,String cell_voltage);

    WxShareOrdersSub getLastOrders(String userId);

    void payCallback(String outTradeNo, Integer payType);

    List<WxShareOrders> getByUserId(String user);

    Map<String,String> getShareOrdersEqNumber(String macno);

    ResultView switchLock(String userId, String equipmentNumber, String swift);


    ResultView creatShareOrders(String userId, WxEquipment equipment, String equipmentNumber);



    WxShareOrders selectByOutTradeNo(String outTradeNo);
}
