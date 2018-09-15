package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.wrap.HospitalEquipment;
import com.sharebedapp.jijl.model.wrap.HospitalInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("wxHospitalMapper")
public interface WxHospitalMapper extends BaseMapper<WxHospital,String> {

    List<HospitalInfo> getHospitalNameList();

    List<HospitalEquipment> getHospitalInfoList(@Param("hospitalProvince") String hospitalProvince,
                                                @Param("hospitalCity") String hospitalCity,
                                                @Param("hospitalName") String hospitalName);
}