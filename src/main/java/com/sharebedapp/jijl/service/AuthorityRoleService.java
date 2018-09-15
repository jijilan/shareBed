package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.AuthorityRole;
import com.sharebedapp.jijl.result.ResultView;


import java.util.List;

/**
* @Description
* @Date 2018/8/20 21:45
* @Author liangshihao
*/
public interface AuthorityRoleService {
    List<AuthorityRole> getAuthorityRoleListByManager(String managerId);

    ResultView setRoleByManager(String managerId,String... roleIds);

    ResultView getRoleList(Integer pageNo,Integer pageSize);

    ResultView addRole(String roleName, String roleNote);

    ResultView delRole(String roleId);

    ResultView getAuthorityByRole(String roleId);
}
