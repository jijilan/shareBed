package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxGoodsOrdersMapper;
import com.sharebedapp.jijl.mapper.WxShareOrdersMapper;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private WxShareOrdersMapper wxShareOrdersMapper;
    @Autowired
    private WxGoodsOrdersMapper wxGoodsOrdersMapper;
    @Override
    public ResultView getSevenToMonth(Integer type, Integer orderType) {
        List<Map<String, Object>> ordersList = null;
        Map<String, Object> map = new HashMap<String, Object>(2);
        //共享
        if (orderType == 1) {
            if (type == 1) {
                ordersList = wxShareOrdersMapper.getSevenOrderList();
            } else if (type == 2) {
                ordersList = wxShareOrdersMapper.getMonthOrderList();
            }
        }
        //销售
            if (orderType == 2) {
                if (type == 1) {
                    ordersList = wxGoodsOrdersMapper.getSevenOrderList();
                } else if (type == 2) {
                    ordersList = wxGoodsOrdersMapper.getMonthOrderList();
                }
            }
            map.put("ordersList", ordersList);
            return ResultView.ok(map);
        }

    }
