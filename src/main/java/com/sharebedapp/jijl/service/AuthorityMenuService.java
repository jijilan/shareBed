package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.AuthorityMenu;
import com.sharebedapp.jijl.model.AuthorityRole;
import com.sharebedapp.jijl.result.ResultView;


import java.util.List;
import java.util.Set;

/**
 * @Author: jijl
 * @Description:
 * @Date: 2018/8/3 14:31
 */
public interface AuthorityMenuService {
    ResultView getAuthorityMenuList(String managerId);

    ResultView setAuthorityByRole(String roleId, String... menus);

    Set<String> getAuthoritysByManager(String managerId);

    ResultView addAuthorityMenu(AuthorityMenu authorityMenu);

    ResultView getMenuByCondition(String fid);

    ResultView updateMenu(AuthorityMenu menu);

    ResultView getMenuInfoById(String id);

    ResultView delMenu(String id);

    int getMenuCountByFid(String id);
}
