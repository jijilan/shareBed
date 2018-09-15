package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxFeedback;
import com.sharebedapp.jijl.model.sub.WxFeedbackSub;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WxFeedbackMapper extends BaseMapper<WxFeedback,String> {

    List<WxFeedbackSub> getFeedbackList(@Param("feedbackType") Integer feedbackType,
                                        @Param("nickName") String nickName,
                                        @Param("status") Integer status,
                                        @Param("startTime")String startTime,
                                        @Param("endTime") String endTime);

    WxFeedbackSub getFeedback(String feedbackId);
}