package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.*;
import com.sharebedapp.jijl.model.wrap.AgentInfo;
import com.sharebedapp.jijl.model.wrap.CategoryInfo;
import com.sharebedapp.jijl.model.wrap.EquipmentInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.service.WxAgentService;
import com.sharebedapp.jijl.utils.DESCode;
import com.sharebedapp.jijl.utils.DataConverter;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 17:35
 */

@Service
public class WxAgentServiceImpl implements WxAgentService {

    @Autowired
    private WxAgentMapper wxAgentMapper;

    @Autowired
    private WxCategoryMapper wxCategoryMapper;

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;

    @Autowired
    private WxHospitalMapper wxHospitalMapper;

    @Autowired
    private WxDeptMapper wxDeptMapper;

    @Autowired
    private WxAftersaleMapper wxAftersaleMapper;

    @Autowired
    private WxShareOrdersMapper wxShareOrdersMapper;

    @Autowired
    private WxFinanceMapper wxFinanceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addAgent(WxAgent wxAgent) {
        wxAgent.setAgentId(IdentityUtil.identityId("AG"));
        WxAgent agent = wxAgentMapper.selectAgentByAccount(wxAgent.getAgentAccount());
        if (agent != null){
            return ResultStatus.AGENT_EXISTED;
        }
        wxAgent.setAgentPassWord(DESCode.encode(wxAgent.getAgentPassWord()));
        wxAgent.setcTime(new Date());
        return wxAgentMapper.insertSelective(wxAgent);
    }

    @Override
    public PageInfo getAgentList(Integer pageNo, Integer pageSize, String roleName, String agentAccount, String agentNickName, Integer proportion, String agentPhone, String hospitalId) {
        PageHelper.startPage(pageNo,pageSize);
        double revenue = 0;
        List<AgentInfo> agentList = wxAgentMapper.selectAgentByCondition(roleName,agentAccount,agentNickName,proportion,agentPhone,hospitalId);
        for (AgentInfo agentInfo : agentList) {
            if (agentInfo.getProportion() != null) {
                BigDecimal agentRevenue = wxFinanceMapper.calculateRevenueByAgentId(agentInfo.getAgentId());
                if (agentRevenue != null){
                    revenue = agentRevenue.doubleValue();
                }
                }
                agentInfo.setRevenue(DataConverter.getTwoDigitNum(revenue));
            }
        return new PageInfo(agentList,pageSize);
    }

    @Override
    public WxAgent login(WxAgent wxAgent) {
        wxAgent.setAgentPassWord(DESCode.encode(wxAgent.getAgentPassWord()));
        return wxAgentMapper.selectByExample(wxAgent).get(0);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updatePassword(String agentId, String agentPhone, String newPassword) {
        WxAgent agent = wxAgentMapper.selectByPrimaryKey(agentId);
        if (!agent.getAgentPhone().equals(agentPhone)){
            return ResultStatus.NOT_EXISTED;
        }
        agent.setAgentPassWord(DESCode.encode(newPassword));
        return wxAgentMapper.updateByPrimaryKeySelective(agent);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateAgent(WxAgent wxAgent) {
        List<WxAgent> wxAgentList = wxAgentMapper.selectByHospitalId(wxAgent.getHospitalId());
        int proportion = 0;
        for (WxAgent agentExisted : wxAgentList) {
            if (wxAgent.getProportion() != null){
                proportion += agentExisted.getProportion();
            }
        }
        WxAgent agent = wxAgentMapper.selectByPrimaryKey(wxAgent.getAgentId());
        if (proportion + wxAgent.getProportion() - agent.getProportion() > 100){
            return ResultStatus.PROPORTION_OVER;
        }
        return wxAgentMapper.updateByPrimaryKeySelective(wxAgent);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updatePhoneNumber(String agentId, String newPhoneNumber) {
        WxAgent agent = wxAgentMapper.selectByAgentPhone(newPhoneNumber);
        if (agent != null){
            return ResultStatus.PHONE_EXISTED;
        }
        WxAgent wxAgent = wxAgentMapper.selectByPrimaryKey(agentId);
        if (wxAgent != null){
            wxAgent.setAgentPhone(newPhoneNumber);
        }

        return wxAgentMapper.updateByPrimaryKeySelective(wxAgent);
    }

    @Override
    public HashMap<String, Object> getAgentEquipmentList(Integer pageNo, Integer pageSize, String hospitalId, String agentId) {
        HashMap<String, Object> map = new HashMap<>();

        WxAgent agent = wxAgentMapper.selectByPrimaryKey(agentId);
        Integer total = 0;

        List<CategoryInfo> categoryInfoList = new LinkedList<>();
        List<WxCategory> wxCategories = wxCategoryMapper.selectByExample(new WxCategory());
        loop:for (WxCategory wxCategory : wxCategories) {
            CategoryInfo categoryInfo = new CategoryInfo();
            if (wxCategory!=null) {
                categoryInfo.setCategoryId(wxCategory.getCategoryId());
                categoryInfo.setCategoryName(wxCategory.getCategoryName());
                if (agent != null) {
                    total = wxEquipmentMapper.countByCategoryId(agent.getHospitalId(),
                            wxCategory.getCategoryId(), null, null, null);
                    if (total == 0){
                        continue loop;
                    }
                    categoryInfo.setTotal(total);
                    categoryInfo.setFailureQuantity(wxEquipmentMapper.countByCategoryId(agent.getHospitalId(),
                            wxCategory.getCategoryId(), null, ResultStatus.EQUIOMENT_STATUS_FAILURE, null));

                }
                categoryInfoList.add(categoryInfo);
            }
        }
        map.put("categoryInfoList",categoryInfoList);

        List<EquipmentInfo> agentEqupimentList = new LinkedList<>();

        WxEquipment wxEquipment = new WxEquipment();
        if(agent != null) {
            wxEquipment.setHospitalId(agent.getHospitalId());
        }
        wxEquipment.setHospitalId(agent.getHospitalId());
        List<WxEquipment> wxEquipments = wxEquipmentMapper.selectByExample(wxEquipment);

        WxHospital wxHospital = wxHospitalMapper.selectByPrimaryKey(agent.getHospitalId());
        loop:for (WxEquipment equipment : wxEquipments) {
            EquipmentInfo agentEqupiment = new EquipmentInfo();
            agentEqupiment.setHospitalName(wxHospital.getHospitalName());
            WxCategory wxCategory = wxCategoryMapper.selectByPrimaryKey(equipment.getCategoryId());
            if (wxCategory == null){
                continue loop;
            }
            agentEqupiment.setCategoryName(wxCategory.getCategoryName());
            agentEqupiment.setEquipmentId(equipment.getEquipmentId());
            agentEqupiment.setEquipmentNumber(equipment.getEquipmentNumber());
            agentEqupiment.setEquipmentStatus(equipment.getEquipmentStatus());
            agentEqupimentList.add(agentEqupiment);
        }
        map.put("equipmentInfoList",JsonUtils.PageInfoToMap(new PageInfo(agentEqupimentList,pageSize),"equipmentList"));
        return map;
    }

    @Override
    public List<WxAgent> getEarningsAgetnList(String hospitalId) {
        return wxAgentMapper.getEarningsAgetnList(hospitalId);
    }

    @Override
    public int updateAgentStatus(String agentId, Integer status) {
        WxAgent wxAgent = new WxAgent();
        wxAgent.setAgentId(agentId);
        wxAgent.setIsFlag(status);
        return wxAgentMapper.updateByPrimaryKeySelective(wxAgent);
    }
}
