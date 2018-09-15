package com.sharebedapp.jijl.controller;


import com.github.liujiebang.pay.wx.config.WxOAuth2Info;
import com.github.liujiebang.pay.wx.service.WxAuthService;
import com.sharebedapp.jijl.exception.MyException;
import com.sharebedapp.jijl.jwt.JWTData;
import com.sharebedapp.jijl.jwt.JwtUtil;
import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.model.WxAgent;
import com.sharebedapp.jijl.model.WxSystem;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.*;
import com.sharebedapp.jijl.utils.DESCode;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.PhoneSMS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: jijl
 * @Description: 登录
 * @Date: 2018/7/2 16:48
 **/
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private JWTData jwtData;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SysManagerService sysManagerService;
    @Autowired
    private WxAgentService wxAgentService;
    @Autowired
    private AuthorityMenuService authorityMenuService;

    /**
     * 微信授权登录   --判断本地是否有token缓存-有缓存话拉去用户信息
     * 没有缓存的话，调授权接口
     * 当授权接口中isFlag=0；未绑定手机，前端拉去微信用户
     * 信息，并绑定手机，isFlag=1 ，已经绑定过手机，就直接
     * 用返回token拉去服务器用户信息
     *
     * @param code
     * @return
     */
    @PostMapping("/authorization")
    public ResultView authorization(String code) {
        log.info("code:{}", code);
        if (StringUtils.isEmpty(code)) {
            return ResultView.error(ResultEnum.CODE_200);
        }
        try {
            WxOAuth2Info wxOAuth2Info = null;
            try {
                wxOAuth2Info = wxAuthService.wxSpOAuth2AccessToken(code);
                log.info("获取openId相关信息---------{}", wxOAuth2Info.getOpenid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("openId:{}", wxOAuth2Info.getOpenid());
            WxUser wxUser = wxUserService.getByOpenId(wxOAuth2Info.getOpenid());
            if (wxUser == null) {
                wxUser = new WxUser();
                String userId = IdentityUtil.identityId("UI");
                wxUser.setUserId(userId);
                wxUser.setIsFlag(ResultStatus.WXUSER_ISFLAG_0);
                wxUser.setUserType(ResultStatus.WXUSER_USERTYPE_1);
                wxUser.setIsPurchaser(ResultStatus.WXUSER_ISPURCHASER_1);
                wxUser.setOpenId(wxOAuth2Info.getOpenid());
                wxUser.setcTime(new Date());
                if (wxUserService.insertWxUser(wxUser) > 0) {
                    log.info("授权成功----------:{}", userId);
                }
            }
            Map<String, Object> map = new HashMap<>(2);
            String token = jwtToken(ResultStatus.USER_ID, wxUser.getUserId(), wxUser);
            if (StringUtils.isEmpty(token)) {
                return ResultView.error(ResultEnum.CODE_9999);
            }
            map.put(ResultStatus.TOKEN, token);
            map.put(ResultStatus.USER, wxUser);
            map.put(ResultStatus.USER_TYPE, ResultStatus.WX_CUSTOMER);
            return ResultView.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.CODE_20);
        }

    }

    /**
     * 用户端 测试用登陆
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/frontLogin")
    public ResultView frontLogin(String userId) {
        WxUser users = wxUserService.getByUserId(userId);
        if (users != null) {
            String jwtToken = jwtToken(ResultStatus.USER_ID, users.getUserId(), users);
            redisService.set(users.getUserId(), users, ResultStatus.TONKEN_OUT_TIME);
            Map<String, Object> map = new HashMap<>();
            map.put(ResultStatus.TOKEN, jwtToken);
            map.put(ResultStatus.USER, users);
            map.put(ResultStatus.USER_TYPE, ResultStatus.WX_CUSTOMER);
            return ResultView.ok(map);
        }
        return ResultView.error(ResultEnum.CODE_4);
    }

    /**
     * 后台登陆
     *
     * @param userAcount 账号
     * @param passWord   密码
     * @return
     */
    @PostMapping(value = "/backLogin")
    public ResultView backLogin(String userAcount, String passWord) {
        if (StringUtils.isBlank(userAcount)) {
            return ResultView.error(ResultEnum.CODE_14);
        }
        if (StringUtils.isBlank(passWord)) {
            return ResultView.error(ResultEnum.CODE_15);
        }
        AuthorityManager sysManager = sysManagerService.backLogin(userAcount, DESCode.encode(passWord));
        if (sysManager != null) {
            Map<String, Object> map = new HashMap<>(2);
            String token = jwtToken(ResultStatus.MANAGER_ID, sysManager.getManagerId(), sysManager);
            if (StringUtils.isEmpty(token)) {
                return ResultView.error(ResultEnum.CODE_9999);
            }
            if (redisService.hasKey(ResultStatus.AUTHORITY + sysManager.getManagerId())){
                redisService.del(ResultStatus.AUTHORITY + sysManager.getManagerId());
            }
            //获取 角色权限集合 存到redis中 redisService.set(ResultStatus.AUTHORITY+managerIdlist)
            Set<String> authorityList = authorityMenuService.getAuthoritysByManager(sysManager.getManagerId());
            if (authorityList != null && authorityList.size() > 0) {
                redisService.set(ResultStatus.AUTHORITY + sysManager.getManagerId(), authorityList);
            }
            map.put(ResultStatus.TOKEN, token);
            map.put(ResultStatus.MANAGER, sysManager);
            return ResultView.ok(map);
        }

        return ResultView.error(ResultEnum.CODE_4);
    }

    /**
     * 获取手机验证码
     *
     * @param phone
     * @param type
     * @return
     */
    @GetMapping("/getPhoneCode")
    public ResultView getPhoneCode(String phone, Integer type) {
        if (StringUtils.isEmpty(phone)) {
            return ResultView.error(ResultEnum.CODE_9);
        }
        if (type == null) {
            return ResultView.error(ResultEnum.CODE_16);
        }
        String phoneCode = IdentityUtil.getRandomNum(6);
        //1:完善用户信息  2:修改手机  3：验证新手机
        String messageModel = PhoneSMS.messageModel(phoneCode, type);
        if (PhoneSMS.sendSMS(messageModel, phone) == 1) {
            redisService.set(ResultStatus.WMWLSHAREBED + phone, phoneCode, 180);
            log.info("给" + phone + "手机号码发送验证码----->" + phoneCode);
            return ResultView.ok();
        } else {
            return ResultView.error(ResultEnum.CODE_17);
        }
    }

    /**
     * 购买商登录
     *
     * @param userPhone
     * @param phoneCode
     * @param code
     * @return
     */
    @PostMapping("/frontBuyerLogin")
    public ResultView frontBuyerLogin(String userPhone, String phoneCode, String code) {
        if (StringUtils.isEmpty(userPhone)) {
            return ResultView.error(ResultEnum.CODE_9);
        }
        if (StringUtils.isEmpty(phoneCode)) {
            return ResultView.error(ResultEnum.CODE_10);
        }
        String phoneCodeTwo = (String) redisService.get(ResultStatus.WMWLSHAREBED + userPhone);

        if (StringUtils.isEmpty(phoneCodeTwo)) {
            return ResultView.error(ResultEnum.CODE_11);
        }

        if (!phoneCodeTwo.equalsIgnoreCase(phoneCode)) {
            return ResultView.error(ResultEnum.CODE_11);
        }
        WxOAuth2Info wxOAuth2Info = null;
        WxUser phonrwxUser = wxUserService.getUserByPhone(userPhone);
        try {
            wxOAuth2Info = wxAuthService.wxSpOAuth2AccessToken(code);
            log.info("获取openId相关信息---------{}", wxOAuth2Info.getOpenid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (phonrwxUser == null) {
            //注册一个用户

            Integer[] isFlags = {1, 2};
            WxUser byOpenIdAnduserPhone = wxUserService.getByOpenIdAndiIsFlag(wxOAuth2Info.getOpenid(), isFlags);
            if (byOpenIdAnduserPhone == null) {
                phonrwxUser = new WxUser();
                phonrwxUser.setUserId(IdentityUtil.identityId("UI"));
                phonrwxUser.setIsFlag(ResultStatus.WXUSER_ISFLAG_0);
                phonrwxUser.setUserType(ResultStatus.WXUSER_USERTYPE_1);
                phonrwxUser.setIsPurchaser(ResultStatus.WXUSER_ISPURCHASER_2);
                phonrwxUser.setOpenId(wxOAuth2Info.getOpenid());
                /**
                 * 如果前端调用微信setUserInfo方法在登陆时给一起绑定微信用户信息，就注释调用户手机号码
                 * 分2步保存
                 */
                //phonrwxUser.setUserPhone(userPhone);
                phonrwxUser.setcTime(new Date());
                wxUserService.insertWxUser(phonrwxUser);
            } else {
                return ResultView.error(ResultEnum.CODE_69);
            }
        } else {

            //是否已经成为购买商
            if (phonrwxUser.getIsPurchaser() == 1) {
                //修改用户为购买商
                phonrwxUser.setIsPurchaser(ResultStatus.WXUSER_ISPURCHASER_2);
                wxUserService.updateWxUser(phonrwxUser);
            }
            if (phonrwxUser.getIsFlag() == ResultStatus.WXUSER_ISFLAG_2) {
                return ResultView.error(ResultEnum.CODE_154);
            }
            if (!phonrwxUser.getOpenId().equals(wxOAuth2Info.getOpenid())) {
                return ResultView.error(ResultEnum.CODE_69);
            }
        }
        HashMap<String, Object> map = new HashMap<>(3);
        String token = jwtToken(ResultStatus.USER_ID, phonrwxUser.getUserId(), phonrwxUser);
        map.put(ResultStatus.TOKEN, token);
        map.put(ResultStatus.USER, phonrwxUser);
        map.put(ResultStatus.USER_TYPE, ResultStatus.WX_BUYER);

        return ResultView.ok(map);
    }

    @PostMapping("/frontAgentLogin")
    public ResultView frontAgentLogin(WxAgent wxAgent) {
        if (StringUtils.isEmpty(wxAgent.getAgentAccount())) {
            return ResultView.error(ResultEnum.CODE_14);
        }
        if (StringUtils.isEmpty(wxAgent.getAgentPassWord())) {
            return ResultView.error(ResultEnum.CODE_15);
        }
        try {
            WxAgent wxAgentLogin = wxAgentService.login(wxAgent);
            if (wxAgentLogin != null) {
                String token = jwtToken(ResultStatus.AGENT_ID, wxAgentLogin.getAgentId(), wxAgentLogin);
                HashMap<String, Object> map = new HashMap<>();
                map.put(ResultStatus.TOKEN, token);
                map.put(ResultStatus.WXAGENT, wxAgentLogin);
                map.put(ResultStatus.USER_TYPE, ResultStatus.WX_AGENT);
                return ResultView.ok(map);
            } else {
                return ResultView.error(ResultEnum.CODE_4);
            }
        } catch (Exception e) {
            return ResultView.error(ResultEnum.CODE_46);
        }
    }


    @PostMapping("/updatePassword")
    public ResultView updatePassword(HttpServletRequest request, String agentPhone, String phoneCode, String newPassword) {
        ResultView resultView = checkPhoneCode(agentPhone, phoneCode);
        String agentId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        if (!resultView.getData().equals(true)) {
            return resultView;
        }
        int flag = wxAgentService.updatePassword(agentId, agentPhone, newPassword);
        if (flag > 0) {
            return ResultView.ok();
        } else if (flag == ResultStatus.NOT_EXISTED) {
            return ResultView.error(ResultEnum.CODE_71);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }


    @PostMapping("/checkPhoneCode")
    public ResultView checkPhoneCode(String phoneNumber, String phoneCode) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return ResultView.error(ResultEnum.CODE_9);
        }
        if (StringUtils.isEmpty(phoneCode)) {
            return ResultView.error(ResultEnum.CODE_10);
        }
        if (redisService.get(ResultStatus.WMWLSHAREBED + phoneNumber) == null) {
            return ResultView.error(ResultEnum.CODE_11);
        }
        String code = (String) redisService.get(ResultStatus.WMWLSHAREBED + phoneNumber);
        if (!code.equalsIgnoreCase(phoneCode)) {
            return ResultView.error(ResultEnum.CODE_11);
        }
        return ResultView.ok(true);
    }

    /**
     * 生成token
     *
     * @param unique   id标识
     * @param uniqueId 用户编号-或管理员编号
     * @param obj      用户对象或管理员对象
     * @return
     */
    private String jwtToken(String unique, String uniqueId, Object obj) {
        String jwtToken = JwtUtil.createJWT(unique,
                uniqueId,
                jwtData.getClientId(),
                jwtData.getName(),
                jwtData.getExpiresSecond(),
                jwtData.getBase64Secret());
        redisService.set(uniqueId, obj, ResultStatus.TONKEN_OUT_TIME);
        return jwtToken;
    }

    @Autowired
    private WxSystemService wxSystemService;

    /***
     * 获取系统参数
     *  1:押金设置 2:客服电话 3.每天可提现次数 4.最低提现金额 5:提现须知
     * @return
     */
    @GetMapping(value = "getPhone")
    public ResultView getPhone() {
        WxSystem wxSystem = wxSystemService.getSystem(2);
        if (wxSystem == null) {
            return ResultView.error(ResultEnum.CODE_157);
        }
        String parameter = wxSystem.getParameter();
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("parameter", parameter);
        return ResultView.ok(map);
    }
}
