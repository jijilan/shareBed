package com.sharebedapp.jijl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sharebedapp.jijl.mapper.WxEquipmentMapper;
import com.sharebedapp.jijl.mapper.WxFinanceMapper;
import com.sharebedapp.jijl.mapper.WxGoodsOrdersMapper;
import com.sharebedapp.jijl.mapper.WxHospitalMapper;
import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxFinance;
import com.sharebedapp.jijl.model.WxGoodsOrders;
import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.wrap.GoodsOrdersInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxGoodsOrdersService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class WxGoodsOrdersServiceImpl implements WxGoodsOrdersService {
    @Autowired
    private WxGoodsOrdersMapper wxGoodsOrdersMapper;

    @Autowired
    private WxHospitalMapper wxHospitalMapper;

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;

    @Autowired
    private WxFinanceMapper wxFinanceMapper;

    @Override
    public ResultView getGoodsOrdersList(String goodsOrdersId, String userPhone, String categoryId, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(wxGoodsOrdersMapper.getGoodsOrdersList(goodsOrdersId,userPhone,categoryId), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"goodsOrdersList"));
    }

    @Override
    public ResultView getGoodsOrders(String goodsOrdersId) {
        HashMap<String,Object> map=new HashMap<>(1);
        map.put("goodsOrders",wxGoodsOrdersMapper.getGoodsOrders(goodsOrdersId));
        return ResultView.ok(map);
    }

    @Override
    public int getGoodsOrderCount(String categoryId) {
        return wxGoodsOrdersMapper.getGoodsOrderCount(categoryId);
    }

    @Override
    public String createGoodsOrders(String hospitalId, String categoryId, Integer totalNum, String agentId, String userId) {
        synchronized (this) {
            WxGoodsOrders goodsOrders = new WxGoodsOrders();
            goodsOrders.setGoodsOrdersId(IdentityUtil.identityId("GO"));
            goodsOrders.setCategoryId(categoryId);
            goodsOrders.setHospitalId(hospitalId);
            goodsOrders.setTotalNum(totalNum);
            Integer availableEquipment = wxEquipmentMapper.getEquipmentAmountByHospital(categoryId,hospitalId);
            if (totalNum > availableEquipment){
                return ResultStatus.FAILED;
            }
            goodsOrders.setAgentId(agentId);
            goodsOrders.setUserId(userId);

            WxHospital wxHospital = wxHospitalMapper.selectByPrimaryKey(hospitalId);
            JSONObject jsonObject = JSONObject.parseObject(wxHospital.getEquipmentPrice());
            goodsOrders.setPrice(new BigDecimal(jsonObject.get(categoryId).toString()));
            goodsOrders.setTotalPrice(new BigDecimal(totalNum * Double.valueOf(jsonObject.get(categoryId).toString())));
            goodsOrders.setOrderStatus(ResultStatus.ORDERS_STATUS_UNPAY);
            goodsOrders.setOutTradeNo(IdentityUtil.identityId("GOU"));
            goodsOrders.setcTime(new Date());

            wxGoodsOrdersMapper.insertSelective(goodsOrders);
            return goodsOrders.getOutTradeNo();
        }
    }

    @Override
    public PageInfo getOrdersList(Integer pageNo, Integer pageSize, Integer orderStatus, String userId) {
        PageHelper.startPage(pageNo,pageSize);
        List<GoodsOrdersInfo> goodsOrdersInfoList = wxGoodsOrdersMapper.selectGoodsOrdersInfo(orderStatus,userId);
        return new PageInfo(goodsOrdersInfoList,pageSize);
    }

    @Override
    public void callback(String outTradeNo, Integer payType) {
        WxGoodsOrders wxGoodsOrders = wxGoodsOrdersMapper.selectByOutTradeNo(outTradeNo);
        wxGoodsOrders.setPayTime(new Date());
        wxGoodsOrders.setOrderStatus(ResultStatus.ORDERS_STATUS_PAY);

        WxFinance wxFinance = new WxFinance();
        wxFinance.setFinanceId(IdentityUtil.identityId("FI"));
        wxFinance.setFinanceType(ResultStatus.FINANCE_TYPE_BUY);
        wxFinance.setOutTradeNo(outTradeNo);
        wxFinance.setUserId(wxGoodsOrders.getUserId());
        wxFinance.setExpensesAmount(wxGoodsOrders.getTotalPrice());
        wxFinance.setPayType(ResultStatus.PAYTYPE_WX);
        wxFinance.setHospitalId(wxGoodsOrders.getHospitalId());
        wxFinance.setIsFlag(ResultStatus.IS_FLAG_VALID);
        wxFinance.setcTime(new Date());
        wxFinanceMapper.insertSelective(wxFinance);

        List<WxEquipment> wxEquipments = wxEquipmentMapper.getEquipmentAvailable(wxGoodsOrders.getTotalNum(),wxGoodsOrders.getHospitalId());
        for (WxEquipment equipment : wxEquipments) {
            equipment.setUserId(wxGoodsOrders.getUserId());
            wxEquipmentMapper.updateByPrimaryKeySelective(equipment);
        }
        wxGoodsOrdersMapper.updateByPrimaryKeySelective(wxGoodsOrders);
    }

    @Override
    public int updateGoodsOrdersStatus(String goodsOrdersId, Integer ordersStatus) {
        WxGoodsOrders wxGoodsOrders = new WxGoodsOrders();
        wxGoodsOrders.setGoodsOrdersId(goodsOrdersId);
        wxGoodsOrders.setOrderStatus(ordersStatus);
        return wxGoodsOrdersMapper.updateByPrimaryKeySelective(wxGoodsOrders);
    }

    @Override
    public WxGoodsOrders selectByOutTradeNo(String outTradeNo) {
        return wxGoodsOrdersMapper.selectByOutTradeNo(outTradeNo);
    }
}
