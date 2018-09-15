package com.sharebedapp.jijl.model;

import java.io.Serializable;
import java.util.Date;

public class WxAgent  implements Serializable {
    /** 代理人编号 */
    private String agentId;

    /** 代理人账号 */
    private String agentAccount;

    /** 代理人密码 */
    private String agentPassWord;

    /** 代理人昵称 */
    private String agentNickName;

    /** 代理人头像 */
    private String agentPic;

    /** 代理人手机 */
    private String agentPhone;

    /** 医院编号 */
    private String hospitalId;

    /** 角色编号 */
    private String roleId;

    /** 1:正常 2：禁用 */
    private Integer isFlag;

    /** 创建时间 */
    private Date cTime;

    //角色分成比例
    private Integer proportion;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId == null ? null : agentId.trim();
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount == null ? null : agentAccount.trim();
    }

    public String getAgentPassWord() {
        return agentPassWord;
    }

    public void setAgentPassWord(String agentPassWord) {
        this.agentPassWord = agentPassWord == null ? null : agentPassWord.trim();
    }

    public String getAgentNickName() {
        return agentNickName;
    }

    public void setAgentNickName(String agentNickName) {
        this.agentNickName = agentNickName == null ? null : agentNickName.trim();
    }

    public String getAgentPic() {
        return agentPic;
    }

    public void setAgentPic(String agentPic) {
        this.agentPic = agentPic == null ? null : agentPic.trim();
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone == null ? null : agentPhone.trim();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId == null ? null : hospitalId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
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

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }
}