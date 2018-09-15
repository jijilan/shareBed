package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxBankcards {
    /** 银行卡标识 */
    private String bankCardId;

    /** 银行卡号 */
    private String bankNumber;

    /** 持卡人姓名 */
    private String bankRealName;

    /** 手机号码 */
    private String phoneNumber;

    /** 所属银行 */
    private String bankName;

    /** 银行卡类型 */
    private String bankCardType;

    /**
     * 用户id
     */
    private String consumerId;

    /** 创建时间 */
    private Date cTime;

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId == null ? null : bankCardId.trim();
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
    }

    public String getBankRealName() {
        return bankRealName;
    }

    public void setBankRealName(String bankRealName) {
        this.bankRealName = bankRealName == null ? null : bankRealName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType == null ? null : bankCardType.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}