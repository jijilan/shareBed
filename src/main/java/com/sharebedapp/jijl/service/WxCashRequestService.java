package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxCashrequest;
import com.sharebedapp.jijl.model.wrap.CashRequest;
import com.sharebedapp.jijl.result.ResultView;

/**
 * @Author: jijl
 * @Date 2018/8/28 11:22
 */
public interface WxCashRequestService {
    int withdrawDeposit(String consumerId, String bankCardId, double amount);

    ResultView getCashRequestList(Integer pageNo, Integer pageSize, String userPhone, String phoneNumber, String bankRealName);

    WxCashrequest getBycashRequestId(String cashRequestId);

    int updCashRequest(WxCashrequest wxCashrequest);

    CashRequest getCashRequest();
}
