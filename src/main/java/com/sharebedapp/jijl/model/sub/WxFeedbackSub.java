package com.sharebedapp.jijl.model.sub;

import java.util.Date;

public class WxFeedbackSub {

    private String nickName;

    /** 意见编号 */
    private String feedbackId;

    /** 意见内容 */
    private String context;

    /** 意见图片 */
    private String feedbackUrl;

    /** 联系电话 */
    private String userPhone;

    /** 1.未处理 2.已处理 */
    private Integer status;

    /** 创建时间 */
    private Date cTime;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}
