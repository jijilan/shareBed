package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxAftersale {
    /** 售后服务编号 */
    private String afterSaleId;

    /** 售后类型【1.保洁 2.报修】 */
    private Integer afterSaleType;

    /** 用户编号 */
    private String userId;



    /** 设备id */
    private String equipmentId;

    /** 金额 */
    private BigDecimal price;

    /** 售后图片，多张","隔开 */
    private String afterSalePrice;

    /** 创建时间 */
    private Date cTime;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId == null ? null : afterSaleId.trim();
    }

    public Integer getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(Integer afterSaleType) {
        this.afterSaleType = afterSaleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAfterSalePrice() {
        return afterSalePrice;
    }

    public void setAfterSalePrice(String afterSalePrice) {
        this.afterSalePrice = afterSalePrice == null ? null : afterSalePrice.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}