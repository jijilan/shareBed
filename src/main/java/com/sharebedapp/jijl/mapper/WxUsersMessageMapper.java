package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxUsersMessage;
import org.springframework.stereotype.Component;


@Component("wxUsersMessageMapper")
public interface WxUsersMessageMapper extends BaseMapper<WxUsersMessage,Integer> {

}