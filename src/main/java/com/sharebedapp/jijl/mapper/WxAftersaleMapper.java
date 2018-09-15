package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxAftersale;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("wxAftersaleMapper")
public interface WxAftersaleMapper extends BaseMapper<WxAftersale,String>{

    List<Map<String,Object>> getAftersaleList(@Param("equipmentId") String equipmentId);
}