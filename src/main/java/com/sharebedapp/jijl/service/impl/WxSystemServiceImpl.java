package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxSystemMapper;
import com.sharebedapp.jijl.model.WxSystem;
import com.sharebedapp.jijl.service.WxSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxSystemServiceImpl implements WxSystemService {

    @Autowired
    private WxSystemMapper wxSystemMapper;
    @Override
    public WxSystem getSystem(Integer systemType) {
        return wxSystemMapper.getSystem(systemType);
    }

    @Override
    public int addSystemParamter(WxSystem wxSystem) {
        return wxSystemMapper.insertSelective(wxSystem);
    }

    @Override
    public int updateSystemParamter(WxSystem wxSystem) {
        return wxSystemMapper.updateByPrimaryKeySelective(wxSystem);
    }
}
