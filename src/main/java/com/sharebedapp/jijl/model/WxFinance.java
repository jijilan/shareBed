package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxFinance {
    /** 财务编号 */
    private String financeId;

    /** 用户编号 */
    private String userId;
    /** 渠道商编号 */
    private String agentId;

    /** 医院编号 */
    private String hospitalId;

    /** 账户余额【balanceType=1为收益余额,balanceType=2为押金】 */
    private BigDecimal balance;

    /** 支付流水号 */
    private String outTradeNo;

    /** 收益金额 */
    private BigDecimal revenueAmount;

    /** 支出金额 */
    private BigDecimal expensesAmount;

    /** 1:充值 2:共享订单 3.购买订单 4.设备收益 5:押金缴纳 6:押金退款 7:提现 */
    private Integer financeType;

    /** 1.余额 2.微信 3.支付宝 */
    private Integer payType;

    /** 财务类型:1:余额 2:保证金 */
    private Integer balanceType;

    /** 1:无效 2:有效 */
    private Integer isFlag;

    /** 创建时间 */
    private Date cTime;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getFinanceId() {
        return financeId;
    }

    public void setFinanceId(String financeId) {
        this.financeId = financeId == null ? null : financeId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public BigDecimal getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(BigDecimal revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public BigDecimal getExpensesAmount() {
        return expensesAmount;
    }

    public void setExpensesAmount(BigDecimal expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    public Integer getFinanceType() {
        return financeType;
    }

    public void setFinanceType(Integer financeType) {
        this.financeType = financeType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Integer balanceType) {
        this.balanceType = balanceType;
    }

    public Integer getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Integer isFlag) {
        this.isFlag = isFlag;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}