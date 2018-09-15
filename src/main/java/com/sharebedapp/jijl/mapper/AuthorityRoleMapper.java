package com.sharebedapp.jijl.mapper;


import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.AuthorityMenuView;
import com.sharebedapp.jijl.model.AuthorityRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorityRoleMapper extends BaseMapper<AuthorityRole,String> {

    List<AuthorityRole> getAuthorityRoleListByManager(@Param("managerId") String managerId);

    int delRoleByManager(@Param("managerId") String managerId);

    int setRoleByManager(@Param("managerId") String managerId,@Param("roleIds") String[] roleIds);

    int delRoleByRoleId(@Param("roleId") String roleId);

    List<AuthorityMenuView> getAuthorityByRole(@Param("interfaceType") Integer interfaceType, @Param("fid") String fid, @Param("roleId") String roleId);
}