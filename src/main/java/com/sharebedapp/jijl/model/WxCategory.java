package com.sharebedapp.jijl.model;

import java.util.Date;

public class WxCategory {
    /** ID标识 */
    private String categoryId;

    /** 设备分类名称 */
    private String categoryName;

    /** 设备分类图片 */
    private String categoryPic;

    /** 设备分类描述 */
    private String categoryDesc;

    /** 创建时间 */
    private Date cTime;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(String categoryPic) {
        this.categoryPic = categoryPic == null ? null : categoryPic.trim();
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc == null ? null : categoryDesc.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
}