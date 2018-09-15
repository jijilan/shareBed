package com.sharebedapp.jijl.model;

public class WxSystem {
    /** 主键 */
    private Integer systemId;

    /** 1:押金设置 2:客服电话 3.每天可提现次数 4.最低提现金额 5:提现须知 */
    private Integer systemType;

    /** 参数值 */
    private String parameter;

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }
}