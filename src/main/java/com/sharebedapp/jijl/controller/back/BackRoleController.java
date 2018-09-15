package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxRole;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxRoleService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: jijl
 * @Date 2018/8/23 14:02
 */
@RestController
@RequestMapping("/back")
public class BackRoleController {

    private final WxRoleService wxRoleService;

    @Autowired
    public BackRoleController(WxRoleService wxRoleService) {
        this.wxRoleService = wxRoleService;
    }

    @PostMapping("/addRole")
    public ResultView addRole(WxRole wxRole){
        if (StringUtils.isEmpty(wxRole.getRoleName())){
            return ResultView.error(ResultEnum.CODE_30);
        }
        if (wxRoleService.addRole(wxRole)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @DeleteMapping("/deleteRole")
    public ResultView deleteRole(String roleId){
        if (StringUtils.isEmpty(roleId)){
            return ResultView.error(ResultEnum.CODE_31);
        }
        if (wxRoleService.deleteRole(roleId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getRoleList")
    public ResultView getRoleList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        PageInfo pageInfo = wxRoleService.getRoleList(pageNo,pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"roleList"));
    }
}
