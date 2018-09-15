package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxMessageMapper;
import com.sharebedapp.jijl.mapper.WxUsersMessageMapper;
import com.sharebedapp.jijl.model.WxMessage;
import com.sharebedapp.jijl.model.WxUsersMessage;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.service.WxMessageService;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 16:35
 */

@Service
public class WxMessageServiceImpl implements WxMessageService {

    @Autowired
    private WxMessageMapper wxMessageMapper;

    @Autowired
    private WxUsersMessageMapper wxUsersMessageMapper;

    @Override
    public PageInfo getMessageList(Integer pageNo, Integer pageSize, String title, Long startTime, Long endTime) {
        PageHelper.startPage(pageNo,pageSize);
        Date start ;
        Date end = new Date();
        if (startTime != null){
            start = DateUtils.getTimeFromLong(startTime);
        }else {
            start = DateUtils.getTimeFromLong(0);
        }
        if (endTime != null){
            end = DateUtils.getTimeFromLong(endTime);
        }

        List<WxMessage> wxMessageList = wxMessageMapper.selectByCondition(title,start,end);
        return new PageInfo(wxMessageList,pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addMessage(WxMessage wxMessage) {
        wxMessage.setMessageId(IdentityUtil.identityId("MG"));
        wxMessage.setIsRead(ResultStatus.IS_READ_UNREAD);
        wxMessage.setcTime(new Date());
        return wxMessageMapper.insertSelective(wxMessage);
    }

    @Override
    public WxMessage getMessageInfo(String consumerId, String messageId) {
        if (consumerId != null) {
            WxUsersMessage usersMessage = new WxUsersMessage();
            usersMessage.setUserId(consumerId);
            usersMessage.setMessageId(messageId);
            wxUsersMessageMapper.insertSelective(usersMessage);
        }
        return wxMessageMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public int deleteById(String messageId) {
        return wxMessageMapper.deleteByPrimaryKey(messageId);
    }
}
