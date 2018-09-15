package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxHospital {
    /** 医院编号 */
    private String hospitalId;

    /** 医院名称 */
    private String hospitalName;

    /** 医院主图 */
    private String hospitalPic;

    /** 联系人 */
    private String contacts;

    /** 联系人电话 */
    private String contactsPhone;

    /** 合同年限【N/年】 */
    private Integer contractPeriod;

    /** 省 */
    private String hospitalProvince;

    /** 市 */
    private String hospitalCity;

    /** 区 */
    private String hospitalArea;

    /** 地址详情 */
    private String hospitalAddress;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** [{"categoryId":"1","price":"500"},{"categoryId":"2","price":"200"}] */
    private String equipmentPrice;

    /** 医院佣金比例(1-99之间) */
    private Integer hospitalCommissionRate;

    /** 购买商佣金比例(1-99之间) */
    private Integer purchaserCommissionRate;

    /** 1:正常 2：禁用 */
    private Integer isFlag;

    /** 创建时间 */
    private Date cTime;

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName == null ? null : hospitalName.trim();
    }

    public String getHospitalPic() {
        return hospitalPic;
    }

    public void setHospitalPic(String hospitalPic) {
        this.hospitalPic = hospitalPic == null ? null : hospitalPic.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone == null ? null : contactsPhone.trim();
    }

    public Integer getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public String getHospitalProvince() {
        return hospitalProvince;
    }

    public void setHospitalProvince(String hospitalProvince) {
        this.hospitalProvince = hospitalProvince == null ? null : hospitalProvince.trim();
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity == null ? null : hospitalCity.trim();
    }

    public String getHospitalArea() {
        return hospitalArea;
    }

    public void setHospitalArea(String hospitalArea) {
        this.hospitalArea = hospitalArea == null ? null : hospitalArea.trim();
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress == null ? null : hospitalAddress.trim();
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getEquipmentPrice() {
        return equipmentPrice;
    }

    public void setEquipmentPrice(String equipmentPrice) {
        this.equipmentPrice = equipmentPrice == null ? null : equipmentPrice.trim();
    }

    public Integer getHospitalCommissionRate() {
        return hospitalCommissionRate;
    }

    public void setHospitalCommissionRate(Integer hospitalCommissionRate) {
        this.hospitalCommissionRate = hospitalCommissionRate;
    }

    public Integer getPurchaserCommissionRate() {
        return purchaserCommissionRate;
    }

    public void setPurchaserCommissionRate(Integer purchaserCommissionRate) {
        this.purchaserCommissionRate = purchaserCommissionRate;
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