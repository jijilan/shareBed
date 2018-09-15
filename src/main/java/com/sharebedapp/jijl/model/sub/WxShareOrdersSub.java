package com.sharebedapp.jijl.model.sub;

import java.util.Date;

public class WxShareOrdersSub {
    private String  shareOrderId;
    private Integer  equipmenLockType;
    private String outTradeNo;
    private Double price;
    private Double availableTimeLength;
    private Date leaseStartTime;
    private Date leaseEndTime;
    private Date startTime;
    private Date endTime;
    private Integer orderStatus;
    private String equipmentNumber;
    private Double rechargePrice;
    private Integer  rechargeType;
    private Double overtimePrice;
    private String deptName;
    private String deptNumber;
    private String wardName;
    private String wardNumber;
    private String hospitalName;
    private String   bedNumber;

    private String   bluetoothName;

    public Integer getEquipmenLockType() {
        return equipmenLockType;
    }

    public void setEquipmenLockType(Integer equipmenLockType) {
        this.equipmenLockType = equipmenLockType;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getShareOrderId() {
        return shareOrderId;
    }

    public void setShareOrderId(String shareOrderId) {
        this.shareOrderId = shareOrderId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAvailableTimeLength() {
        return availableTimeLength;
    }

    public void setAvailableTimeLength(Double availableTimeLength) {
        this.availableTimeLength = availableTimeLength;
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

    public Double getRechargePrice() {
        return rechargePrice;
    }

    public void setRechargePrice(Double rechargePrice) {
        this.rechargePrice = rechargePrice;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Double getOvertimePrice() {
        return overtimePrice;
    }

    public void setOvertimePrice(Double overtimePrice) {
        this.overtimePrice = overtimePrice;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
