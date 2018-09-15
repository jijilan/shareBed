package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxDept {
    /** (科室父节点编号为0) */
    private Integer deptId;

    /** 科室或病房号名称【科室对应:骨科,房号对应:101】 */
    private String deptName;

    /** 栋数或层数名称【科室对应描述是栋数,房号对应描述是层数】 */
    private String deptNumber;

    /** 父ID（科室编号父ID为0） */
    private Integer fid;

    /** 医院编号 */
    private String hospitalId;

    /** 创建时间 */
    private Date cTime;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber == null ? null : deptNumber.trim();
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
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