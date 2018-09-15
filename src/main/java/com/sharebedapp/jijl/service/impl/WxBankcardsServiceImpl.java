package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxBankcardsMapper;
import com.sharebedapp.jijl.model.WxBankcards;
import com.sharebedapp.jijl.service.WxBankcardsService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 22:03
 */

@Service
public class WxBankcardsServiceImpl implements WxBankcardsService {

    @Autowired
    private WxBankcardsMapper wxBankcardsMapper;

    @Override
    public int addBankcards(WxBankcards wxBankcards, String consumerId) {
        wxBankcards.setConsumerId(consumerId);
        wxBankcards.setBankCardId(IdentityUtil.identityId("BA"));
        wxBankcards.setcTime(new Date());
        return wxBankcardsMapper.insertSelective(wxBankcards);
    }

    @Override
    public int deletebankCards(String bankCardId) {
        return wxBankcardsMapper.deleteByPrimaryKey(bankCardId);
    }

    @Override
    public PageInfo getBankcardsList(Integer pageNo, Integer pageSize, String consumerId) {
        PageHelper.startPage(pageNo,pageSize);
        WxBankcards bankcards = new WxBankcards();
        bankcards.setConsumerId(consumerId);
        List<WxBankcards> wxBankcards = wxBankcardsMapper.selectByExample(bankcards);
        return new PageInfo(wxBankcards,pageSize);
    }
}
