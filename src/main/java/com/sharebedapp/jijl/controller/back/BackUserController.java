package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxUserService;
import com.github.liujiebang.pay.wx.config.WxConfig;
import com.github.liujiebang.pay.wx.config.WxRequest;
import com.github.liujiebang.pay.wx.service.WxPayService;
import com.github.liujiebang.pay.wx.service.impl.WxPayServiceImpl;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzm
 */
@RestController
@RequestMapping(value = "/back")
public class BackUserController {

    private final WxUserService wxUserService;

    @Autowired
    public BackUserController(WxUserService wxUserService) {
        this.wxUserService = wxUserService;
    }

    /***
     * 用户列表
     *  @param nickName 昵称
     * @param userPhone 手机号
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping(value = "/getUserList")
    public ResultView getUserList(String nickName, String userPhone, @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return wxUserService.getUserList(nickName, userPhone, pageNo, pageSize);
    }

    /***
     * 查看用户详情
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/getUser")
    public ResultView getUser(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return ResultView.error(ResultEnum.CODE_146);
        }
        return wxUserService.getUser(userId);
    }

    /***
     * 添加保洁人员
     *  @param userId 用户id
     *  @param isFlag 0.授权中 1:正常 2:禁用
     *  @param userType 用户类型【1.普通用户 2.维修人员 3.保洁人员】
     * @return
     */
    @PostMapping(value = "/upIsFlag")
    public ResultView upIsFlag(String userId, Integer isFlag, Integer userType) {
        if (StringUtil.isEmpty(userId)) {
            return ResultView.error(ResultEnum.CODE_146);
        }
        WxUser user = wxUserService.getByUserId(userId);
        if (user == null) {
            return ResultView.error(ResultEnum.CODE_1002);
        }
        if (isFlag != null) {
            if (isFlag != 1 && isFlag != 2) {
                return ResultView.error(ResultEnum.CODE_415);
            }
            user.setIsFlag(isFlag);
        }
        return wxUserService.updateWxUser(user) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    /***
     * 用户添加保洁人员
     *  @param userId 用户id
     *  @param  companyName 公司名称
     *  @param  companyPhone 公司电话
     *  @param userType 用户类型【1.普通用户 2.维修人员 3.保洁人员】
     * @return
     */
    @PostMapping(value = "/upUserType")
    public ResultView upUserType(String userId, String companyName, String companyPhone, Integer userType) {
        if (StringUtil.isEmpty(userId)) {
            return ResultView.error(ResultEnum.CODE_146);
        }
        if (StringUtil.isEmpty(companyName)) {
            return ResultView.error(ResultEnum.CODE_174);
        }
        if (StringUtil.isEmpty(companyPhone)) {
            return ResultView.error(ResultEnum.CODE_175);
        }
        WxUser user = wxUserService.getByUserId(userId);
        if (user == null) {
            return ResultView.error(ResultEnum.CODE_1002);
        }
        if (userType != null) {
            if (userType != 2 && userType != 3) {
                return ResultView.error(ResultEnum.CODE_415);
            }
            user.setUserType(userType);
            user.setCompanyName(companyName);
            user.setCompanyPhone(companyPhone);
        }
        return wxUserService.updateWxUser(user) > 0 ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }

    public static void main(String[] args) {
        WxConfig wxConfig=new WxConfig();
        //小程序
        wxConfig.setWxSpAppId("wx68bc669921ecc06f");
        wxConfig.setWxSpSecrect("c05a76c0775add85844185e8725ddf8b");
        wxConfig.setWxSpMchId("1511300781");
        wxConfig.setWxSpMchKey("123456789zxcvbnmasdfghjklqwertyu");
        wxConfig.setWxSpCertPath("D:/javadata/SZJAVA/czjsharebed/cert/apiclient_cert.p12");
        //统一回调地址
        wxConfig.setNotifyUrl("https://czjsharebed.jijl-sz.com/wcPay/notify");
        WxPayService wxPayService=new WxPayServiceImpl();
        wxPayService.setWxConfigStorage(wxConfig);
        System.out.println(wxPayService.wxReturn("BAI911122340567937",0.01,0.01, WxRequest.WX_SP_PAY));
    }
}
