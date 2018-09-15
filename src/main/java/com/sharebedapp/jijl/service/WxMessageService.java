package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxMessage;
import com.github.pagehelper.PageInfo;

/**
 * @Author: jijl
 * @Date 2018/8/23 16:35
 */
public interface WxMessageService {
    PageInfo getMessageList(Integer pageNo, Integer pageSize, String title, Long startTime, Long endTime);

    int addMessage(WxMessage wxMessage);

    WxMessage getMessageInfo(String consumerId, String messageId);

    int deleteById(String messageId);

}
