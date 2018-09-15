package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxRecharge {
    /** 收费规则表 */
    private String rechargeId;

    /** 医院编号 */
    private String hospitalId;

    /** 收费规则:【1.小时 2.次数】 */
    private Integer rechargeType;

    /** 收费金额【15/小时 或 15/次】 */
    private BigDecimal rechargePrice;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 开始时间原点值 */
    private Long startTimeOriginValue;

    /** 结束时间原点值 */
    private Long endTimeOriginValue;

    /** 最少使用小时数【收费规则为1时有效】 */
    private Integer minHour;

    /** 超时收费标准【20/小时】 */
    private BigDecimal overtimePrice;

    /** 创建时间 */
    private Date cTime;

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId == null ? null : rechargeId.trim();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public BigDecimal getRechargePrice() {
        return rechargePrice;
    }

    public void setRechargePrice(BigDecimal rechargePrice) {
        this.rechargePrice = rechargePrice;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getStartTimeOriginValue() {
        return startTimeOriginValue;
    }

    public void setStartTimeOriginValue(Long startTimeOriginValue) {
        this.startTimeOriginValue = startTimeOriginValue;
    }

    public Long getEndTimeOriginValue() {
        return endTimeOriginValue;
    }

    public void setEndTimeOriginValue(Long endTimeOriginValue) {
        this.endTimeOriginValue = endTimeOriginValue;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public BigDecimal getOvertimePrice() {
        return overtimePrice;
    }

    public void setOvertimePrice(BigDecimal overtimePrice) {
        this.overtimePrice = overtimePrice;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}