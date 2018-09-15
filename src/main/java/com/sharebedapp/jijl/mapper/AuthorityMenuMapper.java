package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.AuthorityMenu;
import com.sharebedapp.jijl.model.AuthorityMenuView;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authorityMenuMapper")
public interface AuthorityMenuMapper extends BaseMapper<AuthorityMenu, String> {

    List<AuthorityMenuView> getMenuList(@Param("interfaceType") Integer interfaceType, @Param("fid") String fid, @Param("managerId") String managerId);

    int delAuthorityById(@Param("roleId") String roleId, @Param("menuId") String menuId);

    int setAuthorityByRole(@Param("roleId") String roleId, @Param("menus") String[] menus);

    List<String> getAuthoritysByManager(@Param("managerId") String managerId);


}