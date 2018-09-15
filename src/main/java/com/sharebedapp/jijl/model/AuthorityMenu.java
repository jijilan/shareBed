package com.sharebedapp.jijl.model;


import com.sharebedapp.jijl.utils.IdentityUtil;
import org.apache.commons.lang3.StringUtils;

public class AuthorityMenu {
    /**  */
    private String id;

    /** 接口名称 */
    private String interfaceName;

    /** 接口地址 */
    private String interfaceUrl;

    /** 页面地址 */
    private String pageUrl;

    /** 父id */
    private String fid;

    /** 接口类型(1:模块 2:菜单 3:按钮) */
    private Integer interfaceType;

    /** 排序 */
    private Integer orderBy;

    /** 1:有效  2:无效 */
    private Integer isFlag;

    public AuthorityMenu() {
    }

    public AuthorityMenu(String name, String interfaceUrl, String pageUrl, Integer type, String fid, Integer orderBy) {
        this.id = IdentityUtil.identityId("MEN");
        this.interfaceName = name;
        this.interfaceUrl =StringUtils.isEmpty(interfaceUrl) ? null : interfaceUrl;
        this.pageUrl =StringUtils.isEmpty(pageUrl) ? null : pageUrl;
        this.fid = StringUtils.isEmpty(fid) ? null : fid;
        this.interfaceType = type;
        this.orderBy = orderBy;
        this.isFlag = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
    }

    public String getInterfaceUrl() {
        return interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl == null ? null : interfaceUrl.trim();
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid == null ? null : fid.trim();
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Integer isFlag) {
        this.isFlag = isFlag;
    }
}