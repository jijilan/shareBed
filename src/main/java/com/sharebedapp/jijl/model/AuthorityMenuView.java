package com.sharebedapp.jijl.model;

import lombok.Data;

import java.util.List;

@Data
public class AuthorityMenuView {
    /**  */
    private String id;

    /** 接口名称 */
    private String interfaceName;

    /** 页面地址 */
    private String pageUrl;

    private List<AuthorityMenuView> buttonList;

    /** 是否有权限（0：没有 大于有该权限） */
    private int isFlag;

    /** 接口类型(1:模块 2:菜单 3:按钮) */
    private int interfaceType;

}