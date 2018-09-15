package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.wrap.HospitalEquipment;
import com.sharebedapp.jijl.model.wrap.HospitalInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/22 15:59
 */

public interface WxHospitalService {

    PageInfo getHospitalList(Integer pageNo, Integer pageSize, String hospitalName, String contacts);

    WxHospital getHospitalInfo(String hospitalId);

    int addHospital(WxHospital wxHospital, String recharge);

    List<HospitalInfo> getHospitalNameList();

    PageInfo getHospitalInfoList(Integer pageNo, Integer pageSize, String hospitalProvince, String hospitalCity,String hospitalArea , String hospitalName);

    WxHospital getByHospitalId(String hospitalId);

    int forbidHospital(String hospitalId ,Integer status);

    HospitalEquipment getHospitalEquipment(Integer pageNo, Integer pageSize, String hospitalId);

}
