package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxDept;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("wxDeptMapper")
public interface WxDeptMapper extends BaseMapper<WxDept,Integer> {

    List<WxDept> selectBySickBedByWard(Integer fid);
}