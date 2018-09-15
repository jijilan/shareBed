package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxSystem;

public interface WxSystemService {
    WxSystem getSystem(Integer type);

    int addSystemParamter(WxSystem wxSystem);

    int updateSystemParamter(WxSystem wxSystem);
}
