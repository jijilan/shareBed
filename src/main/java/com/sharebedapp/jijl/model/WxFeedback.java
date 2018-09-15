package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxFeedback {
    /** 意见编号 */
    private String feedbackId;

    /** 用户编号 */
    private String userId;

    /** 意见类型【1.设备问题 2.支付问题 3.其他问题】 */
    private Integer feedbackType;

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

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId == null ? null : feedbackId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl == null ? null : feedbackUrl.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
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