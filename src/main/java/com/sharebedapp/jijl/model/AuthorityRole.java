package com.sharebedapp.jijl.model;

import com.sharebedapp.jijl.utils.IdentityUtil;

import java.util.Date;

public class AuthorityRole {
    /**  */
    private String id;

    /** 角色名称 */
    private String roleName;

    /** 备注 */
    private String roleNote;

    /**  */
    private Date ctime;

    /** 1:有效  2:无效 */
    private Integer isFlag;

    public AuthorityRole() {
    }

    public AuthorityRole(String roleName, String roleNote) {
        this.id = IdentityUtil.identityId("ROL");
        this.roleName = roleName;
        this.roleNote = roleNote;
        this.ctime = new Date();
        this.isFlag = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleNote() {
        return roleNote;
    }

    public void setRoleNote(String roleNote) {
        this.roleNote = roleNote == null ? null : roleNote.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Integer getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Integer isFlag) {
        this.isFlag = isFlag;
    }
}