package com.sharebedapp.jijl.service.impl;



import com.sharebedapp.jijl.mapper.AuthorityMenuMapper;
import com.sharebedapp.jijl.model.AuthorityMenu;
import com.sharebedapp.jijl.model.AuthorityMenuView;
import com.sharebedapp.jijl.model.AuthorityRole;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.AuthorityMenuService;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: jijl
 * @Description:
 * @Date: 2018/8/3 14:31
 */
@Service
public class AuthorityMenuServiceImpl implements AuthorityMenuService {

    @Autowired
    private AuthorityMenuMapper authorityMenuMapper;

    /**
     * @Description 查询所有菜单
     * @Date 2018/8/20 17:35
     * @Author liangshihao
     */
    @Override
    public ResultView getAuthorityMenuList(String managerId) {
        JSONArray jsonArray = new JSONArray();
        AuthorityMenu authorityMenu = new AuthorityMenu();
        authorityMenu.setInterfaceType(1);
        authorityMenu.setIsFlag(1);
        //查询所有模块
        List<AuthorityMenuView> menuList = authorityMenuMapper.getMenuList(ResultStatus.MENUTYPE_MODULE, null, managerId);
        for (int i = 0; i < menuList.size(); i++) {
            //查询模块下的所有菜单
            List<AuthorityMenuView> nodeList = authorityMenuMapper.getMenuList(ResultStatus.MENUTYPE_MENU, menuList.get(i).getId(), managerId);
            if (nodeList != null && nodeList.size() > 0) {
                for (int k = 0; k < nodeList.size(); k++) {
                    authorityMenu.setFid(nodeList.get(k).getId());
                    //查询菜单中所有的按钮
                    List<AuthorityMenuView> bottunList = authorityMenuMapper.getMenuList(ResultStatus.MENUTYPE_BUTTON, nodeList.get(k).getId(), managerId);
                    nodeList.get(k).setButtonList(bottunList);
                }
                menuList.get(i).setButtonList(nodeList);
            } else {
                List<AuthorityMenuView> bottunList = authorityMenuMapper.getMenuList(ResultStatus.MENUTYPE_BUTTON, menuList.get(i).getId(), managerId);
                menuList.get(i).setButtonList(bottunList);
            }
            jsonArray.add(menuList.get(i));
        }
        return ResultView.ok(jsonArray);
    }

    /**
     * @Description 更新角色权限
     * @Date 2018/8/20 19:55
     * @Author liangshihao
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView setAuthorityByRole(String roleId, String... menus) {
        return (authorityMenuMapper.delAuthorityById(roleId,null) >= 0 && authorityMenuMapper.setAuthorityByRole(roleId, menus) >= 0)
                ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    /**
     * @Description 获取管理员的接口权限集合
     * @Date 2018/8/20 20:32
     * @Author liangshihao
     */
    @Override
    public Set<String> getAuthoritysByManager(String managerId) {
        List<String> authoritysByManager = authorityMenuMapper.getAuthoritysByManager(managerId);
        Set<String> list = new HashSet<String>();
        if (authoritysByManager != null && authoritysByManager.size() > 0) {
            for (String str : authoritysByManager) {
                if (str.contains(",")) {
                    String[] authority = str.split(",");
                    for (String path : authority) {
                        list.add(path);
                    }
                } else {
                    list.add(str);
                }
            }
            return list;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView addAuthorityMenu(AuthorityMenu authorityMenu) {
        return authorityMenuMapper.insertSelective(authorityMenu) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public ResultView getMenuByCondition(String fid) {
        Integer type = null;
        if (StringUtils.isEmpty(fid)) {
            type = ResultStatus.MENUTYPE_MODULE;
        } else {
            type = ResultStatus.MENUTYPE_MENU;
        }
        return ResultView.ok(authorityMenuMapper.getMenuList(type, fid, null));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView updateMenu(AuthorityMenu menu) {
        return authorityMenuMapper.updateByPrimaryKeySelective(menu) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public ResultView getMenuInfoById(String id) {
        return ResultView.ok(authorityMenuMapper.selectByPrimaryKey(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView delMenu(String id) {
        return (authorityMenuMapper.delAuthorityById(null,id) >= 0 && authorityMenuMapper.deleteByPrimaryKey(id) >= 0)
                ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    /**
     * 获取该菜单下是否有子集菜单
     * @param id
     * @return
     */
    @Override
    public int getMenuCountByFid(String id) {
        AuthorityMenu authorityMenu = new AuthorityMenu();
        authorityMenu.setFid(id);
        authorityMenu.setIsFlag(ResultStatus.ISFLAG_Y);
        List<AuthorityMenu> authorityMenus = authorityMenuMapper.selectByExample(authorityMenu);
        if (authorityMenus == null || authorityMenus.size() == 0){
            return 0;
        }
        return authorityMenus.size();
    }
}
