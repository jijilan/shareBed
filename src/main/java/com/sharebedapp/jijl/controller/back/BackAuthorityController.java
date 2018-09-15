package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.model.AuthorityMenu;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.AuthorityMenuService;
import com.sharebedapp.jijl.service.AuthorityRoleService;
import com.sharebedapp.jijl.service.SysManagerService;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 权限控制器
 * @Date 2018/8/21 16:35
 * @Author liangshihao
 */
@Slf4j
@RestController
@RequestMapping("/back/authority")
public class BackAuthorityController {

    private final AuthorityMenuService authorityMenuService;
    private final AuthorityRoleService authorityRoleService;
    private final SysManagerService sysManagerService;

    @Autowired
    public BackAuthorityController(AuthorityMenuService authorityMenuService, AuthorityRoleService authorityRoleService, SysManagerService sysManagerService) {
        this.authorityMenuService = authorityMenuService;
        this.authorityRoleService = authorityRoleService;
        this.sysManagerService = sysManagerService;
    }

    /**
     * @Description 获取权限菜单列表
     * @Date 2018/8/21 15:19
     * @Author liangshihao
     */
    @GetMapping("/getAuthorityMenuList")
    public ResultView getAuthorityMenuList(String managerId, HttpServletRequest request) {
        if (StringUtils.isEmpty(managerId)) {
            managerId = (String) request.getAttribute(ResultStatus.MANAGER_ID);
        }
        return authorityMenuService.getAuthorityMenuList(managerId);
    }

    /**
     * @Description 获取角色的权限菜单列表
     * @Date 2018/8/21 15:19
     * @Author liangshihao
     */
    @GetMapping("/getAuthorityByRole")
    public ResultView getAuthorityByRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return ResultView.error(ResultEnum.CODE_31);
        }
        return authorityRoleService.getAuthorityByRole(roleId);
    }

    /**
     * @Description 获取菜单详情
     * @Date 2018/9/3 9:33
     * @Author liangshihao
     */
    @GetMapping("/getMenuInfoById")
    public ResultView getMenuInfoById(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultView.error(ResultEnum.CODE_179);
        }
        return authorityMenuService.getMenuInfoById(id);
    }

    /**
     * @Description 修改权限菜单
     * @Date 2018/9/3 9:26
     * @Author liangshihao
     */
    @PutMapping("/updateMenu")
    public ResultView updateMenu(AuthorityMenu menu) {
        if (StringUtils.isEmpty(menu.getId())) {
            return ResultView.error(ResultEnum.CODE_179);
        }
        return authorityMenuService.updateMenu(menu);
    }

    /**
     * @Description 删除权限菜单
     * @Date 2018/9/3 9:26
     * @Author liangshihao
     */
    @DeleteMapping("/delMenuById")
    public ResultView delMenu(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultView.error(ResultEnum.CODE_179);
        }
        if (authorityMenuService.getMenuCountByFid(id) > 0) {
            return ResultView.error(ResultEnum.CODE_189);
        }
        return authorityMenuService.delMenu(id);
    }

    /**
     * @Description 设置角色权限
     * @Date 2018/8/21 15:25
     * @Author liangshihao
     */
    @PutMapping("/setAuthorityByRole")
    public ResultView setAuthorityByRole(String roleId, String... menus) {
        if (StringUtils.isEmpty(roleId)) {
            return ResultView.error(ResultEnum.CODE_31);
        }
        if (menus == null || menus.length == 0) {
            return ResultView.error(ResultEnum.CODE_179);
        }
        return authorityMenuService.setAuthorityByRole(roleId, menus);
    }

    /**
     * @Description 获取角色列表
     * @Date 2018/8/21 15:42
     * @Author liangshihao
     */
    @GetMapping("/getRoleList")
    public ResultView getRoleList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return authorityRoleService.getRoleList(pageNo,pageSize);
    }

    /**
     * @Description 获取管理员的角色列表
     * @Date 2018/8/21 15:29
     * @Author liangshihao
     */
    @GetMapping("/getAuthorityRoleListByManager")
    public ResultView getAuthorityRoleListByManager(String managerId) {
        if (StringUtils.isEmpty(managerId)) {
            return ResultView.error(ResultEnum.CODE_53);
        }
        return ResultView.ok(authorityRoleService.getAuthorityRoleListByManager(managerId));
    }

    /**
     * @Description 设置管理员的权限角色
     * @Date 2018/8/21 16:11
     * @Author liangshihao
     */
    @PostMapping("/setRoleByManager")
    public ResultView setRoleByManager(String managerId, String... roleIds) {
        if (StringUtils.isEmpty(managerId)) {
            return ResultView.error(ResultEnum.CODE_53);
        }
        if (roleIds.length == 0) {
            return ResultView.error(ResultEnum.CODE_31);
        }
        return authorityRoleService.setRoleByManager(managerId, roleIds);
    }

    /**
     * @Description 添加权限菜单
     * @Date 2018/8/21 16:29
     * @Author liangshihao
     */
    @PostMapping("/addAuthorityMenu")
    public ResultView addAuthorityMenu(String interfaceName, String interfaceUrl, String pageUrl, Integer interfaceType, String fid,
                                       @RequestParam(value = "orderBy", required = false, defaultValue = "99") Integer orderBy) {
        if (StringUtils.isEmpty(interfaceName)) {
            return ResultView.error(ResultEnum.CODE_177);
        }
        if (interfaceType == null || (ResultStatus.MENUTYPE_MODULE != interfaceType && ResultStatus.MENUTYPE_MENU != interfaceType
                && ResultStatus.MENUTYPE_BUTTON != interfaceType)) {
            return ResultView.error(ResultEnum.CODE_178);
        }
        if ((ResultStatus.MENUTYPE_MENU == interfaceType || ResultStatus.MENUTYPE_BUTTON == interfaceType)&&StringUtil.isEmpty(fid)){
            return ResultView.error(ResultEnum.CODE_178);
        }
        AuthorityMenu authorityMenu = new AuthorityMenu(interfaceName, interfaceUrl, pageUrl, interfaceType, fid, orderBy);
        return authorityMenuService.addAuthorityMenu(authorityMenu);
    }

    /**
     * @Description 获取模块或者菜单
     * @Date 2018/8/22 11:06
     * @Author liangshihao
     */
    @GetMapping("/getMenuByCondition")
    public ResultView getMenuByCondition(String fid) {
        return authorityMenuService.getMenuByCondition(fid);
    }

    /**
     * @Description 添加角色
     * @Date 2018/8/31 20:34
     * @Author liangshihao
     */
    @PostMapping("/addRole")
    public ResultView addRole(String roleName, String roleNote) {
        if (StringUtils.isEmpty(roleName)) {
            return ResultView.error(ResultEnum.CODE_30);
        }
        if (StringUtils.isEmpty(roleNote)) {
            return ResultView.error(ResultEnum.CODE_176);
        }
        return authorityRoleService.addRole(roleName, roleNote);
    }

    /**
     * @Description 删除角色
     * @Date 2018/8/31 20:34
     * @Author liangshihao
     */
    @DeleteMapping("/delRole")
    public ResultView delRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return ResultView.error(ResultEnum.CODE_31);
        }
        return authorityRoleService.delRole(roleId);
    }



    /**
     * @Description 获取管理员列表
     * @Date 2018/6/19 22:03
     * @Author liangshihao
     */
    @GetMapping("/getManagetList")
    public ResultView getManagetList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return sysManagerService.getManagetList(pageNo, pageSize);
    }

    /**
     * @Description 删除管理员
     * @Date 2018/6/30 14:57
     * @Author liangshihao
     */
    @DeleteMapping("/delManageById")
    public ResultView delManageById(String managerId) {
        if (StringUtils.isEmpty(managerId)) {
            return ResultView.error(ResultEnum.CODE_53);
        }
        return sysManagerService.delManageById(managerId);
    }
}
