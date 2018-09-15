package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxRole {
    /** 渠道商角色编号 */
    private String roleId;

    /** 渠道商角色名称 */
    private String roleName;

    /** 创建时间 */
    private Date cTime;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}