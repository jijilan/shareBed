package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxAgent;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 17:34
 */
public interface WxAgentService {
    int addAgent(WxAgent wxAgent);

    PageInfo getAgentList(Integer pageNo, Integer pageSize, String roleName, String agentAccount, String agentNickName, Integer proportion, String userPhone, String hospitalId);

    WxAgent login(WxAgent wxAgent);

    int updatePassword(String agentId, String agentPhone, String newPassword);

    int updateAgent(WxAgent wxAgent);

    int updatePhoneNumber(String agentId, String newPhoneNumber);

    HashMap<String,Object> getAgentEquipmentList(Integer pageNo, Integer pageSize, String hospitalId, String agentId);

    List<WxAgent> getEarningsAgetnList(String hospitalId);

    int updateAgentStatus(String agentId, Integer status);
}
