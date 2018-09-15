package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxMessage {
    /** 消息编号 */
    private String messageId;

    /** 消息类型 1：我的消息 2：通知 */
    private Integer messageType;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String context;

    /** 1:未读 2:已读(针对通知) */
    private Integer isRead;

    /** 创建时间 */
    private Date cTime;

    /** 用户编号 */
    private String userId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}