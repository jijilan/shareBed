package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxFeedback;
import com.sharebedapp.jijl.result.ResultView;

public interface WxFeedbackService {
    int insertFeedback(WxFeedback wxFeedback);

    ResultView getFeedbackList(Integer feedbackType, String nickName, Integer status, String startTime, String endTime, Integer pageNo, Integer pageSize);

    ResultView getFeedback(String feedbackId);

    int upFeedback(String feedbackId);
}
