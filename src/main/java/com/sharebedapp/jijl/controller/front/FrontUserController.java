package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.exception.MyException;
import com.sharebedapp.jijl.mapper.WxAgentMapper;
import com.sharebedapp.jijl.model.WxAgent;
import com.sharebedapp.jijl.model.WxFinance;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxAgentService;
import com.sharebedapp.jijl.service.WxFinanceService;
import com.sharebedapp.jijl.service.WxUserService;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(value = "/front")
public class FrontUserController {

    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WxFinanceService wxFinanceService;
    @Autowired
    private WxAgentService wxAgentService;
    @Autowired
    private WxAgentMapper wxAgentMapper;

    /**
     * 完善用户信息
     *
     * @param wxUser    用户信息
     * @param phoneCode 验证码
     * @return
     */
    @PostMapping(value = "/setUserInfo")
    public ResultView setUserInfo(WxUser wxUser, String phoneCode, HttpServletRequest request) {
        String userPhone = wxUser.getUserPhone();
        if (StringUtils.isEmpty(userPhone)) {
            return ResultView.error(ResultEnum.CODE_9);
        }
        if (StringUtils.isEmpty(phoneCode)) {
            return ResultView.error(ResultEnum.CODE_10);
        }

        String code = (String) redisService.get(ResultStatus.WMWLSHAREBED + userPhone);
        if (StringUtils.isEmpty(code)) {
            return ResultView.error(ResultEnum.CODE_202);
        }
        if (!phoneCode.equals(code)) {
            return ResultView.error(ResultEnum.CODE_11);
        }

        WxUser byOpenIdAnduserPhone = wxUserService.getByUserPhone(wxUser.getUserPhone());
        if (byOpenIdAnduserPhone == null) {
            String userId = (String) request.getAttribute(ResultStatus.USER_ID);
            WxUser upWxUser = wxUserService.getByUserId(userId);
            if (upWxUser == null){
                log.info("获取用户信息失败！");
                throw new MyException(ResultEnum.CODE_73);
            }
            upWxUser.setAvatarUrl(wxUser.getAvatarUrl());
            upWxUser.setNickName(wxUser.getNickName());
            upWxUser.setGender(wxUser.getGender());
            upWxUser.setCity(wxUser.getCity());
            upWxUser.setProvince(wxUser.getProvince());
            upWxUser.setCountry(wxUser.getCountry());
            upWxUser.setUserPhone(userPhone);
            upWxUser.setIsFlag(ResultStatus.WXUSER_ISFLAG_1);
            if (wxUserService.updateWxUser(upWxUser) > 0) {
                redisService.set(upWxUser.getUserId(), upWxUser);
                return ResultView.ok();
            }
        }
        return ResultView.error(ResultEnum.CODE_70);
    }

    /***
     * 获取用户信息
     * @param request
     * @return
     */
    @GetMapping(value = "/getUserInfo")
    public ResultView getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxUser wxUser = (WxUser) redisService.get(userId);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("user", wxUser);
        return ResultView.ok(map);
    }

    /***
     * 查询押金
     * @param request
     * @return
     */
    @GetMapping(value = "/isBail")
    public ResultView isBail(HttpServletRequest request) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxFinance finance = wxFinanceService.isBail(userId);
        if (finance == null) {
            return ResultView.ok(0);
        } else {
            if (finance.getFinanceType() != ResultStatus.WXFINACE_FINANCEETYPE_5) {
                return ResultView.ok(0);
            } else {
                return ResultView.ok(finance.getBalance());
            }
        }

    }

    /***
     * 支付押金
     * @param request
     * @param price 押金
     * @return
     */
    @PostMapping(value = "/payForBail")
    public ResultView payForBail(HttpServletRequest request, String price) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        return wxFinanceService.payForBail(userId, price);
    }

    /***
     * 退还保证金
     * @param request
     * @return
     */
    @PostMapping(value = "/backBail")
    public ResultView backBail(HttpServletRequest request) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        return wxFinanceService.backBail(userId);
    }

    /***
     * 修改手机
     * @param phoneCode 验证码
     * @return
     */
    @PostMapping(value = "/verifyPhone")
    public ResultView verifyPhone(HttpServletRequest request, String phoneCode) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxUser user = wxUserService.getByUserId(userId);
        String userPhone = user.getUserPhone();
        String code = (String) redisService.get(ResultStatus.WMWLSHAREBED + userPhone);
        if (StringUtils.isEmpty(code)) {
            return ResultView.error(ResultEnum.CODE_202);
        }
        if (!phoneCode.equals(code)) {
            return ResultView.error(ResultEnum.CODE_11);
        }
        return ResultView.ok();
    }

    /***
     * 验证手机号
     * @param phone 电话
     * @param phoneCode 验证码
     * @return
     */
    @PostMapping(value = "/updatePhone")
    public ResultView updatePhone(HttpServletRequest request, String phone, String phoneCode) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        if (userId != null) {
            WxUser user = wxUserService.getByUserId(userId);
            if (StringUtil.isEmpty(phone)) {
                return ResultView.error(ResultEnum.CODE_9);
            }
            WxUser byUserPhone = wxUserService.getUserByPhone(phone);
            if (byUserPhone != null) {
                return ResultView.error(ResultEnum.CODE_70);
            }
            String code = (String) redisService.get(ResultStatus.WMWLSHAREBED + phone);
            if (StringUtils.isEmpty(code)) {
                return ResultView.error(ResultEnum.CODE_202);
            }
            if (!phoneCode.equals(code)) {
                return ResultView.error(ResultEnum.CODE_11);
            }
            user.setUserPhone(phone);
            if (wxUserService.updateWxUser(user) > 0) {
                redisService.set(user.getUserId(), user);
                return ResultView.ok();
            }
        }else {
            String agentId = (String) request.getAttribute(ResultStatus.AGENT_ID);
            WxAgent wxAgent = wxAgentMapper.selectByPrimaryKey(agentId);
            String code = (String) redisService.get(ResultStatus.WMWLSHAREBED + phone);
            if (StringUtils.isEmpty(code)) {
                return ResultView.error(ResultEnum.CODE_202);
            }
            if (!phoneCode.equals(code)) {
                return ResultView.error(ResultEnum.CODE_11);
            }
            int flag = wxAgentService.updatePhoneNumber(agentId, phone);
            if (flag > 0) {
                wxAgent.setAgentPhone(phone);
                redisService.set(wxAgent.getAgentId(), wxAgent);
                return ResultView.ok();
            } else if (flag == ResultStatus.PHONE_EXISTED) {
                return ResultView.error(ResultEnum.CODE_72);
            }
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
