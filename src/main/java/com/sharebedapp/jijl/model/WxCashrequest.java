package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxCashrequest {
    /** 提现编号 */
    private String cashRequestId;

    /** 渠道商提现标识 */
    private String agentId;

    /** 购买商提现标识 */
    private String userId;

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

    /** 提现金额 */
    private BigDecimal amount;

    /** 1.审核中 2.通过 3.驳回 */
    private Integer status;

    /** 提现类型 1 余额提现 2 收益提现 */
    private Integer cashRequestType;

    /** 驳回理由 */
    private String nayReason;

    /** 申请时间 */
    private Date cTime;

    /** 处理时间 */
    private Date uTime;

    public String getCashRequestId() {
        return cashRequestId;
    }

    public void setCashRequestId(String cashRequestId) {
        this.cashRequestId = cashRequestId == null ? null : cashRequestId.trim();
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId == null ? null : agentId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCashRequestType() {
        return cashRequestType;
    }

    public void setCashRequestType(Integer cashRequestType) {
        this.cashRequestType = cashRequestType;
    }

    public String getNayReason() {
        return nayReason;
    }

    public void setNayReason(String nayReason) {
        this.nayReason = nayReason == null ? null : nayReason.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }
}