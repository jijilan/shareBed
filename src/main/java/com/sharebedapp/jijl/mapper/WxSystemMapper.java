package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxSystem;
import org.springframework.stereotype.Component;

@Component("wxSystemMapper")
public interface WxSystemMapper extends BaseMapper<WxSystem,Integer> {

    WxSystem getSystem(Integer systemType);
}