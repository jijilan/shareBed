package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxRecharge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("wxRechargeMapper")
public interface WxRechargeMapper extends BaseMapper<WxRecharge,String> {

    List<WxRecharge> getByRecharge(@Param("hospitalId") String hospitalId);

    List<Integer> getRechargeTypeListByHospitalId(String hospitalId);
}