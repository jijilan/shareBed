package com.sharebedapp.jijl.service.impl;



import com.alibaba.fastjson.JSONArray;
import com.sharebedapp.jijl.mapper.AuthorityRoleMapper;
import com.sharebedapp.jijl.model.AuthorityMenu;
import com.sharebedapp.jijl.model.AuthorityMenuView;
import com.sharebedapp.jijl.model.AuthorityRole;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.AuthorityRoleService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 角色
 * @Author: jijl
 * @Date: 2018/8/3 14:31
 */
@Service
public class AuthorityRoleServiceImpl implements AuthorityRoleService {


    @Autowired
    private AuthorityRoleMapper authorityRoleMapper;

    /**
     * @Description 获取用户的角色列表
     * @Date 2018/8/20 21:59
     * @Author liangshihao
     */
    @Override
    public List<AuthorityRole> getAuthorityRoleListByManager(String managerId) {
        return authorityRoleMapper.getAuthorityRoleListByManager(managerId);
    }

    /**
     * @Description 设置管理员的权限角色
     * @Date 2018/8/21 15:43
     * @Author liangshihao
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView setRoleByManager(String managerId, String... roleIds) {
        return (authorityRoleMapper.delRoleByManager(managerId) >= 0 && authorityRoleMapper.setRoleByManager(managerId, roleIds) > 0) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }


    @Override
    public ResultView getRoleList(Integer pageNo,Integer pageSize) {
        AuthorityRole role = new AuthorityRole();
        role.setIsFlag(1);
        List<AuthorityRole> authorityRoles = authorityRoleMapper.selectByExample(role);
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(authorityRoles,pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"RoleList"));
    }

    /**
     * @Description 新增角色
     * @Date 2018/8/21 15:43
     * @Author liangshihao
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView addRole(String roleName, String roleNote) {
        AuthorityRole authorityRole = new AuthorityRole(roleName, roleNote);
        return authorityRoleMapper.insertSelective(authorityRole) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    /**
     * @Description 删除角色
     * @Date 2018/8/31 20:44
     * @Author liangshihao
     */
    @Override
    public ResultView delRole(String roleId) {
        return (authorityRoleMapper.delRoleByRoleId(roleId) >= 0 && authorityRoleMapper.deleteByPrimaryKey(roleId) >= 0) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public ResultView getAuthorityByRole(String roleId) {
        JSONArray jsonArray = new JSONArray();
        AuthorityMenu authorityMenu = new AuthorityMenu();
        authorityMenu.setInterfaceType(1);
        authorityMenu.setIsFlag(1);
        //查询所有模块
        List<AuthorityMenuView> menuList = authorityRoleMapper.getAuthorityByRole(ResultStatus.MENUTYPE_MODULE, null, roleId);
        for (int i = 0; i < menuList.size(); i++) {
            //查询模块下的所有菜单
            List<AuthorityMenuView> nodeList = authorityRoleMapper.getAuthorityByRole(ResultStatus.MENUTYPE_MENU, menuList.get(i).getId(), roleId);
            if (nodeList != null && nodeList.size() > 0) {
                for (int k = 0; k < nodeList.size(); k++) {
                    authorityMenu.setFid(nodeList.get(k).getId());
                    //查询菜单中所有的按钮
                    List<AuthorityMenuView> bottunList = authorityRoleMapper.getAuthorityByRole(ResultStatus.MENUTYPE_BUTTON, nodeList.get(k).getId(), roleId);
                    nodeList.get(k).setButtonList(bottunList);
                }
                menuList.get(i).setButtonList(nodeList);
            } else {
                List<AuthorityMenuView> bottunList = authorityRoleMapper.getAuthorityByRole(ResultStatus.MENUTYPE_BUTTON, menuList.get(i).getId(), roleId);
                menuList.get(i).setButtonList(bottunList);
            }
            jsonArray.add(menuList.get(i));
        }
        return ResultView.ok(jsonArray);
    }



}
