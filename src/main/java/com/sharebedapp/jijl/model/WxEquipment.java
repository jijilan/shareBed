package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxEquipment {
    /** 设备主键 */
    private String equipmentId;
    /** 设备锁类型 1蓝牙 2机械 */
    private Integer equipmenLockType;

    /** 设备类型 */
    private String categoryId;

    /** 医院编号 */
    private String hospitalId;

    /** 科室编号【对应wx_dept deptId】 */
    private Integer departmentId;

    /** 病房编号【对应wx_dept deptId】 */
    private Integer wardId;

    /** 床位编号 */
    private Integer bedNumber;

    /** 设备唯一编码 */
    private String equipmentNumber;

    /** 设备状态 1：空闲 2：使用中 3.故障 */
    private Integer equipmentStatus;

    /** 用户购买后绑定userId */
    private String userId;

    /** 是否绑定【1:未绑定 2：已绑定】 */
    private Integer isBinding;

    /** 特征值 */
    private String characteristicValue;

    /** uuid */
    private String uuid;

    /** 蓝牙名称 */
    private String bluetoothName;

    /** 创建时间 */
    private Date cTime;

    private String cellVoltage;

    public Integer getEquipmenLockType() {
        return equipmenLockType;
    }

    public void setEquipmenLockType(Integer equipmenLockType) {
        this.equipmenLockType = equipmenLockType;
    }

    public String getCellVoltage() {
        return cellVoltage;
    }

    public void setCellVoltage(String cellVoltage) {
        this.cellVoltage = cellVoltage;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId == null ? null : equipmentId.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber == null ? null : equipmentNumber.trim();
    }

    public Integer getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Integer equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getIsBinding() {
        return isBinding;
    }

    public void setIsBinding(Integer isBinding) {
        this.isBinding = isBinding;
    }

    public String getCharacteristicValue() {
        return characteristicValue;
    }

    public void setCharacteristicValue(String characteristicValue) {
        this.characteristicValue = characteristicValue == null ? null : characteristicValue.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName == null ? null : bluetoothName.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}