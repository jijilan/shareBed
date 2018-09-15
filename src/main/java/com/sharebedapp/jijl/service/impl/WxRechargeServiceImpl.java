package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxRechargeMapper;
import com.sharebedapp.jijl.model.WxRecharge;
import com.sharebedapp.jijl.model.wrap.RechargeInfo;
import com.sharebedapp.jijl.model.wrap.TimeRechargeInfo;
import com.sharebedapp.jijl.service.WxRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WxRechargeServiceImpl implements WxRechargeService {
    @Autowired
    private WxRechargeMapper wxRechargeMapper;
    @Override
    public List<WxRecharge> getByRecharge(String hospitalId) {
        return wxRechargeMapper.getByRecharge(hospitalId);
    }

    @Override
    public WxRecharge getByRechargeId(String rechargeId) {
        return wxRechargeMapper.selectByPrimaryKey(rechargeId);
    }

    @Override
    public List<RechargeInfo> getRechargeList(String hospitalId) {
        List<RechargeInfo> rechargeInfoList = new LinkedList<>();
        WxRecharge wxRecharge = new WxRecharge();
        wxRecharge.setHospitalId(hospitalId);
        List<WxRecharge> wxRechargeList = wxRechargeMapper.selectByExample(wxRecharge);
        List<Integer> typeList = wxRechargeMapper.getRechargeTypeListByHospitalId(hospitalId);
        for (Integer type : typeList) {
            RechargeInfo rechargeInfo = new RechargeInfo();
            rechargeInfo.setRechargeType(type);
            List<TimeRechargeInfo> timeRechargeInfoList = new LinkedList<>();
            rechargeInfo.setTimeRechargeInfoList(timeRechargeInfoList);
            rechargeInfoList.add(rechargeInfo);
        }
        for (RechargeInfo info : rechargeInfoList) {
            for (WxRecharge recharge : wxRechargeList) {
                if (info.getRechargeType() == recharge.getRechargeType()) {
                    if (info.getMinHour() == null) {
                        info.setMinHour(recharge.getMinHour());
                    }
                    if(info.getOvertimePrice() == null){
                        info.setOvertimePrice(recharge.getOvertimePrice());
                    }

                    TimeRechargeInfo timeRechargeInfo = new TimeRechargeInfo();
                    timeRechargeInfo.setStartTime(recharge.getStartTime().toString().substring(11, 16));
                    timeRechargeInfo.setEndTime(recharge.getEndTime().toString().substring(11, 16));
                    timeRechargeInfo.setRechargePrice(recharge.getRechargePrice().doubleValue());

                    info.getTimeRechargeInfoList().add(timeRechargeInfo);
                }
            }
        }
        return rechargeInfoList;
    }
}
