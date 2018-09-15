package com.sharebedapp.jijl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.WxAgent;
import com.sharebedapp.jijl.model.WxCategory;
import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.WxRecharge;
import com.sharebedapp.jijl.model.wrap.CategoryInfo;
import com.sharebedapp.jijl.model.wrap.HospitalEquipment;
import com.sharebedapp.jijl.model.wrap.HospitalInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.service.WxHospitalService;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.GouldAPI;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/22 15:59
 */

@Service
public class WxHospitalServiceImpl implements WxHospitalService {

    @Autowired
    private WxHospitalMapper wxHospitalMapper;

    @Autowired
    private WxRechargeMapper wxRechargeMapper;

    @Autowired
    private WxCategoryMapper wxCategoryMapper;

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;

    @Autowired
    private WxAgentMapper wxAgentMapper;

    @Value("${web.portrait-path}")
    private String portraitPath;

    @Override
    public PageInfo getHospitalList(Integer pageNo, Integer pageSize, String hospitalName, String contacts) {
        PageHelper.startPage(pageNo,pageSize);
        WxHospital wxHospital = new WxHospital();
        wxHospital.setHospitalName(hospitalName);
        wxHospital.setContacts(contacts);

        List<WxHospital> wxHospitals = wxHospitalMapper.selectByExample(wxHospital);

        return new PageInfo(wxHospitals,pageSize);
    }

    @Override
    public WxHospital getHospitalInfo(String hospitalId) {
        return wxHospitalMapper.selectByPrimaryKey(hospitalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addHospital(WxHospital wxHospital, String recharge) {
        wxHospital.setHospitalId(IdentityUtil.identityId("HP"));
        JSONObject jsonObject = JSONObject.parseObject(GouldAPI.addressToCoordinate(wxHospital.getHospitalAddress()));
        wxHospital.setLatitude(new BigDecimal(jsonObject.getString("lat")));
        wxHospital.setLongitude(new BigDecimal(jsonObject.getString("lng")));
        wxHospital.setIsFlag(ResultStatus.IS_FLAG_NORMAL);

        List<WxRecharge> wxRecharges = JsonUtils.jsonToList(recharge, WxRecharge.class);

        long startTimeFront = 0;
        long endTimeFront  = 0;
        long startTimeAfter  ;
        long endTimeAfter ;

        for (WxRecharge wxRecharge : wxRecharges) {

            if (startTimeFront == 0){
                startTimeFront = wxRecharge.getStartTime().getTime();
                endTimeFront = wxRecharge.getEndTime().getTime();
            }else {
                startTimeAfter = wxRecharge.getStartTime().getTime();
                endTimeAfter = wxRecharge.getEndTime().getTime();
                if(wxRecharges != null || wxRecharges.size() >0){
                    for (int i = 0; i < wxRecharges.size() ; i++) {
                        if(DateUtils.isIntersection(startTimeFront,endTimeFront,startTimeAfter,endTimeAfter)){
                            return ResultStatus.INTERVAL_REPEAT;
                        }
                    }
                }
            }
            wxRecharge.setRechargeId(IdentityUtil.identityId("RC"));
            wxRecharge.setHospitalId(wxHospital.getHospitalId());
            long[] originValue = DateUtils.calculationOriginValue(wxRecharge.getStartTime().getTime(), wxRecharge.getEndTime().getTime());
            wxRecharge.setStartTimeOriginValue(originValue[0]);
            wxRecharge.setEndTimeOriginValue(originValue[1]);
            wxRecharge.setcTime(new Date());

            wxRechargeMapper.insertSelective(wxRecharge);
        }
        wxHospital.setcTime(new Date());
        return wxHospitalMapper.insertSelective(wxHospital);
    }

    @Override
    public List<HospitalInfo> getHospitalNameList() {
        return wxHospitalMapper.getHospitalNameList();
    }

    @Override
    public PageInfo getHospitalInfoList(Integer pageNo, Integer pageSize, String hospitalProvince, String hospitalCity,String hospitalArea , String hospitalName) {
        PageHelper.startPage(pageNo,pageSize);
        List<HospitalEquipment> hospitalEquipmentList = new LinkedList<>();
        WxHospital wxHospital = new WxHospital();
        wxHospital.setHospitalProvince(hospitalProvince);
        wxHospital.setHospitalCity(hospitalCity);
        wxHospital.setHospitalName(hospitalName);
        wxHospital.setHospitalArea(hospitalArea);
        List<WxHospital> hospitalList = wxHospitalMapper.selectByExample(wxHospital);
        for (WxHospital hospital : hospitalList) {
            if (hospital!=null) {
                HospitalEquipment hospitalEquipment = new HospitalEquipment();
                hospitalEquipment.setHospitalId(hospital.getHospitalId());
                hospitalEquipment.setHospitalCity(hospital.getHospitalCity());
                hospitalEquipment.setHospitalName(hospital.getHospitalName());
                hospitalEquipment.setHospitalAddress(hospital.getHospitalAddress());
                hospitalEquipment.setHospitalPic(hospital.getHospitalPic());
                hospitalEquipment.setCategoryInfoList(getCategoryInfoList(hospital));
                hospitalEquipmentList.add(hospitalEquipment);
            }
        }

        return new PageInfo(hospitalEquipmentList,pageSize);
    }

    @Override
    public WxHospital getByHospitalId(String hospitalId) {
        return wxHospitalMapper.selectByPrimaryKey(hospitalId);
    }

    @Override
    public int forbidHospital(String hospitalId , Integer status) {
        int flag = 0;
        List<WxAgent> wxAgentList = wxAgentMapper.selectByHospitalId(hospitalId);
        if (wxAgentList.size()==0){
            flag = -1;
        }
        for (WxAgent wxAgent : wxAgentList) {
            wxAgent.setIsFlag(status);
            if(wxAgentMapper.updateByPrimaryKeySelective(wxAgent)>0){
                flag = 1;
            }else {
                flag = 0;
            }
        }
        return flag;
    }

    @Override
    public HospitalEquipment getHospitalEquipment(Integer pageNo, Integer pageSize, String hospitalId) {
        HospitalEquipment hospitalEquipment = new HospitalEquipment();
        WxHospital wxHospital = wxHospitalMapper.selectByPrimaryKey(hospitalId);
        if (wxHospital != null) {
            hospitalEquipment.setHospitalId(wxHospital.getHospitalId());
            hospitalEquipment.setHospitalName(wxHospital.getHospitalName());
            hospitalEquipment.setHospitalAddress(wxHospital.getHospitalAddress());
            hospitalEquipment.setHospitalCity(wxHospital.getHospitalCity());
            hospitalEquipment.setHospitalPic(wxHospital.getHospitalPic());
            hospitalEquipment.setCategoryInfoList(getCategoryInfoList(wxHospital));
        }
        return hospitalEquipment;
    }

    private List<CategoryInfo> getCategoryInfoList(WxHospital hospital){
        JSONObject jsonObject = JSONObject.parseObject(hospital.getEquipmentPrice());

        List<WxCategory> categoryList = wxCategoryMapper.selectByExample(new WxCategory());
        List<CategoryInfo> categoryInfoList = new LinkedList<>();
        loop:for (WxCategory wxCategory : categoryList) {
            CategoryInfo categoryInfo = new CategoryInfo();
            if (wxCategory != null) {
                categoryInfo.setCategoryId(wxCategory.getCategoryId());
                categoryInfo.setCategoryName(wxCategory.getCategoryName());
                categoryInfo.setCategoryDesc(wxCategory.getCategoryDesc());
                categoryInfo.setCategoryPic(wxCategory.getCategoryPic());
                Object price = jsonObject.get(wxCategory.getCategoryId());
                if (price == null || Double.valueOf(price.toString()) == 0) {
                    continue loop;
                } else {
                    categoryInfo.setPrice(Double.valueOf(price.toString()));
                }
                Integer total = wxEquipmentMapper.countTotalByCategoryId(hospital.getHospitalId(), wxCategory.getCategoryId());
                categoryInfo.setTotal(total);

                categoryInfo.setAvailableQuantity(wxEquipmentMapper.countAvailableByCategoryId(hospital.getHospitalId(),wxCategory.getCategoryId()));

                categoryInfoList.add(categoryInfo);
            }
        }
        return categoryInfoList;
    }
}
