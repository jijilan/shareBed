package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxFeedbackMapper;
import com.sharebedapp.jijl.model.WxFeedback;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFeedbackService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class WxFeedbackServiceImpl implements WxFeedbackService {
    @Autowired
    private WxFeedbackMapper wxFeedbackMapper;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertFeedback(WxFeedback wxFeedback) {
        return wxFeedbackMapper.insertSelective(wxFeedback);
    }

    @Override
    public ResultView getFeedbackList(Integer feedbackType, String nickName, Integer status, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(wxFeedbackMapper.getFeedbackList(feedbackType,nickName,status,startTime,endTime), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"feedbackList"));
    }

    @Override
    public ResultView getFeedback(String feedbackId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("feedbackInfo",  wxFeedbackMapper.getFeedback(feedbackId));
        return ResultView.ok(map);
    }

    @Override
    public int upFeedback(String feedbackId) {
        WxFeedback wxFeedback = wxFeedbackMapper.selectByPrimaryKey(feedbackId);
        wxFeedback.setStatus(2);
        return wxFeedbackMapper.updateByPrimaryKeySelective(wxFeedback);
    }
}
