package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxRoleMapper;
import com.sharebedapp.jijl.model.WxRole;
import com.sharebedapp.jijl.service.WxRoleService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: jijl
 * @Date 2018/8/23 14:03
 */
@Service
public class WxRoleServiceImpl implements WxRoleService {

    @Autowired
    private WxRoleMapper wxRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRole(WxRole wxRole) {
        wxRole.setRoleId(IdentityUtil.identityId("RO"));
        wxRole.setcTime(new Date());
        return wxRoleMapper.insertSelective(wxRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRole(String roleId) {
        return wxRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public PageInfo getRoleList(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        return new PageInfo(wxRoleMapper.selectByExample(new WxRole()),pageSize);
    }
}
