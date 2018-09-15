package com.sharebedapp.jijl.model;

import java.math.BigDecimal;
import java.util.Date;

public class WxGoodsOrders {
    /** 设备订单编号 */
    private String goodsOrdersId;

    /** 总价格 */
    private BigDecimal totalPrice;

    /** 总数量 */
    private Integer totalNum;

    /** 单价 */
    private BigDecimal price;

    /** 用户编号 */
    private String userId;
    /** 分类编号 */
    private String categoryId;
    /** 渠道商编号 */
    private String agentId;
    /** 订单状态【1:未支付 2.已支付 3.已取消】 */
    private Integer orderStatus;

    /** 支付流水号 */
    private String outTradeNo;

    /** 支付类型:1:余额 2:微信 3:支付宝 */
    private Integer payType;

    /** 支付时间 */
    private Date payTime;

    /** 医院编号 */
    private String hospitalId;

    /** 创建时间 */
    private Date cTime;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsOrdersId() {
        return goodsOrdersId;
    }

    public void setGoodsOrdersId(String goodsOrdersId) {
        this.goodsOrdersId = goodsOrdersId == null ? null : goodsOrdersId.trim();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}