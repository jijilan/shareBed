package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxShareOrders {
    /** 订单编号 */
    private String shareOrderId;

    /** 设备编号 */
    private String equipmentId;

    /** 收费规则id */
    private String rechargeId;

    /** 医院编号 */
    private String hospitalId;

    /** 租用开始时间 */
    private Date leaseStartTime;

    /** 租用结束时间 */
    private Date leaseEndTime;

    /** 支付费用 */
    private BigDecimal price;

    /** 支付流水号 */
    private String outTradeNo;

    /** 可用时长 */
    private Long availableTimeLength;

    /** 支付时间 */
    private Date payTime;

    /** 支付类型(1-余额  2-微信 3-支付宝) */
    private Integer payType;

    /** 订单状态:1:待支付 2:进行中 3:已完成 */
    private Integer orderStatus;

    /** 用户编号 */
    private String userId;

    /** 套餐开始时间 */
    private Date startTime;

    /** 套餐结束时间 */
    private Date endTime;

    /** 1:无效 2:有效 */
    private Integer isFlag;

    /** 创建时间 */
    private Date cTime;

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getShareOrderId() {
        return shareOrderId;
    }

    public void setShareOrderId(String shareOrderId) {
        this.shareOrderId = shareOrderId == null ? null : shareOrderId.trim();
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId == null ? null : equipmentId.trim();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public Date getLeaseStartTime() {
        return leaseStartTime;
    }

    public void setLeaseStartTime(Date leaseStartTime) {
        this.leaseStartTime = leaseStartTime;
    }

    public Date getLeaseEndTime() {
        return leaseEndTime;
    }

    public void setLeaseEndTime(Date leaseEndTime) {
        this.leaseEndTime = leaseEndTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Long getAvailableTimeLength() {
        return availableTimeLength;
    }

    public void setAvailableTimeLength(Long availableTimeLength) {
        this.availableTimeLength = availableTimeLength;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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