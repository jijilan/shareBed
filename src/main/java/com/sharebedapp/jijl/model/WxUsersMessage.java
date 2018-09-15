package com.sharebedapp.jijl.model;

public class WxUsersMessage {
    /** 主键 */
    private Integer umId;

    /** 用户编号 */
    private String userId;

    /** 消息编号 */
    private String messageId;

    public Integer getUmId() {
        return umId;
    }

    public void setUmId(Integer umId) {
        this.umId = umId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }
}