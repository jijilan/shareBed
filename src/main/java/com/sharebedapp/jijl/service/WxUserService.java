package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.result.ResultView;

import java.util.HashMap;

public interface WxUserService {
    WxUser getByOpenId(String openId);

    int insertWxUser(WxUser wxUser);

    WxUser getByUserId(String userId);

    int updateWxUser(WxUser upWxUser);


    ResultView getUserList(String nickName, String userPhone, Integer pageNo, Integer pageSize);

    ResultView getUser(String userId);

    WxUser getByUserPhone(String userPhone);

    HashMap<String,Object> getUserEquipmentList(Integer pageNo, Integer pageSize, String hospitalId, String userId);

    WxUser getUserByPhone(String userPhone);

    int updatePhoneNumber(String userId, String oldPhoneNumber, String newPhoneNumber);

    WxUser getByOpenIdAnduserPhone(String openId, String userPhone);

    WxUser getByOpenIdAndiIsFlag(String openId,Integer [] isFlags);

    void delUserByTaskUverdue();
}
