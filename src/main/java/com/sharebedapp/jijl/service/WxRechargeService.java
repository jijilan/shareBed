package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxRecharge;
import com.sharebedapp.jijl.model.wrap.RechargeInfo;

import java.util.List;

public interface WxRechargeService {
    List<WxRecharge> getByRecharge(String hospitalId);

    WxRecharge getByRechargeId(String rechargeId);

    List<RechargeInfo> getRechargeList(String hospitalId);
}
