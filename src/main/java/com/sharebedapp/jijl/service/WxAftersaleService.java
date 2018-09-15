package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxAftersale;
import com.sharebedapp.jijl.result.ResultView;

public interface WxAftersaleService {
    ResultView getAftersaleList(String equipmentId, Integer pageNo, Integer pageSize);

    int addAftersale(WxAftersale wxAftersale);

    int delAftersale(String afterSaleId);
}
