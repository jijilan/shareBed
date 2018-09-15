package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxBankcards;
import com.github.pagehelper.PageInfo;

/**
 * @Author: jijl
 * @Date 2018/8/23 22:03
 */
public interface WxBankcardsService {
    int addBankcards(WxBankcards wxBankcards, String consumerId);

    int deletebankCards(String bankCardId);

    PageInfo getBankcardsList(Integer pageNo, Integer pageSize, String consumerId);
}
