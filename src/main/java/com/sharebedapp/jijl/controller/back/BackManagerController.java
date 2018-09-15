package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.SysManagerService;
import com.sharebedapp.jijl.utils.DESCode;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzm
 */
@RestController
@RequestMapping("/back")
public class BackManagerController {
    private final SysManagerService sysManagerService;
    private final RedisService redisService;

    @Autowired
    public BackManagerController(SysManagerService sysManagerService, RedisService redisService) {
        this.sysManagerService = sysManagerService;
        this.redisService = redisService;
    }

    /**
     *  退出登录(后台)
     * @param request
     * @return
     */
    @GetMapping("/getExitLogin")
    public ResultView getExitLogin(HttpServletRequest request){
        String managerId = (String) request.getAttribute(ResultStatus.MANAGER_ID);
        redisService.del(managerId);
        return  ResultView.ok();
    }

    /**
     * 新增管理员
     * @param userName 用户名称
     * @param userAcount 用户账号
     * @param passWord 用户密码
     * @return
     */
    @PostMapping("/authority/addManager")
    public ResultView addManager(String userName, String userAcount, String passWord) {
        AuthorityManager manager = new AuthorityManager();
        if (StringUtils.isEmpty(userName)) {
            return ResultView.error(ResultEnum.CODE_43);
        } else {
            if(StringUtil.isEmpty(userAcount)){
                return ResultView.error(ResultEnum.CODE_14);
            }
            if (sysManagerService.getByUserAcount(userAcount) == 0) {
                manager.setUserAcount(userAcount);
            } else {
                return ResultView.error(ResultEnum.CODE_180);
            }
        }
        if (StringUtils.isEmpty(passWord)){
            return ResultView.error(ResultEnum.CODE_15);
        } else {
            manager.setPassWord(DESCode.encode(passWord));
        }
        manager.setUserName(userName);
        manager.setIsFlag(1);
        manager.setcTime(new Date());
        //1超级管理员  3管理员
        manager.setManagerType(3);
        manager.setManagerId(IdentityUtil.identityId("MA"));
        return sysManagerService.addManager(manager) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    /**
     * 修改管理员密码
     * @param oldPwd    旧密码
     * @param newPwd    新密码
     * @return
     */
    @PostMapping(value = "/updateManagerPwd")
    public ResultView getManagerInfo(HttpServletRequest request, String oldPwd, String newPwd) {
        String managerId = (String) request.getAttribute(ResultStatus.MANAGER_ID);
        AuthorityManager manager = (AuthorityManager) redisService.get(managerId);
        if(manager != null ){
            if(manager.getPassWord().equals(DESCode.encode(oldPwd))){
                manager.setPassWord(DESCode.encode(newPwd));
                if(sysManagerService.updateManagerPwd(manager) > 0){
                    redisService.del(managerId);
                    return ResultView.ok();
                }else{
                    return ResultView.error(ResultEnum.CODE_2);
                }
            }else{
                return ResultView.error(ResultEnum.CODE_166);
            }
        }else {
            return ResultView.error(ResultEnum.CODE_2);
        }
    }
    /**
     * 获取当前登录的管理员信息
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/managerInfo")
    public ResultView getManagerInfo(HttpServletRequest request) {
        String managerId = (String) request.getAttribute(ResultStatus.MANAGER_ID);
        AuthorityManager manager = (AuthorityManager) redisService.get(managerId);
        Map map = new HashMap();
        map.put("manager",manager);
        return ResultView.ok(map);
    }

}
