package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxAftersaleMapper;
import com.sharebedapp.jijl.model.WxAftersale;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxAftersaleService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WxAftersaleServiceImpl implements WxAftersaleService {
    @Autowired
    private WxAftersaleMapper wxAftersaleMapper;
    @Override
    public ResultView getAftersaleList(String equipmentId,Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(wxAftersaleMapper.getAftersaleList(equipmentId), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"aftersaleList"));
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addAftersale(WxAftersale wxAftersale) {
        return wxAftersaleMapper.insertSelective(wxAftersale);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delAftersale(String afterSaleId) {
        return wxAftersaleMapper.deleteByPrimaryKey(afterSaleId);
    }
}
