package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxRole;
import com.github.pagehelper.PageInfo;

/**
 * @Author: jijl
 * @Date 2018/8/23 14:03
 */
public interface WxRoleService {
    int addRole(WxRole wxRole);

    int deleteRole(String roleId);

    PageInfo getRoleList(Integer pageNo, Integer pageSize);
}
