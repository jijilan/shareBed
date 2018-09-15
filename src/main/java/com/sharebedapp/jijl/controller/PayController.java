package com.sharebedapp.jijl.controller;



import com.sharebedapp.jijl.mapper.WxFinanceMapper;
import com.sharebedapp.jijl.model.WxFinance;
import com.sharebedapp.jijl.model.WxGoodsOrders;
import com.sharebedapp.jijl.model.WxShareOrders;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.service.WxFinanceService;
import com.sharebedapp.jijl.service.WxShareOrdersService;
import com.sharebedapp.jijl.service.WxGoodsOrdersService;
import com.sharebedapp.jijl.service.WxUserService;
import com.github.liujiebang.pay.utils.XMLUtil;
import com.github.liujiebang.pay.wx.service.WxPayService;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * @Author: jijl
 * @Description: 支付
 * @Date: 2018/7/2 16:30
 **/
@Slf4j
@RestController
public class PayController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxFinanceService wxFinanceService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxFinanceMapper wxFinanceMapper;
    @Autowired
    private WxGoodsOrdersService wxGoodsOrdersService;
    @Autowired
    private WxShareOrdersService wxShareOrdersService;
    /**
     * 支付
     *
     * @param outTradeNo 支付流水号
     * @return
     */
    @PostMapping(value = "/front/pay")
    public ResultView pay(HttpServletRequest request,String outTradeNo) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxUser user = wxUserService.getByUserId(userId);
        String sub = outTradeNo.substring(0, 3);
        Map map = null;
        if (StringUtils.isEmpty(outTradeNo)){
            return ResultView.error(ResultEnum.CODE_205);
        }
        if (sub.equalsIgnoreCase("BAI")) {
            //支付押金
            WxFinance wxFinance = wxFinanceMapper.selectByOutTradeNo(outTradeNo);
             map= wxPayService.wxSpPay(wxFinance.getOutTradeNo(), 0.01, user.getOpenId());
        }
        if (sub.equalsIgnoreCase("SHA")) {
            //支付共享订单
            WxShareOrders wxShareOrders = wxShareOrdersService.selectByOutTradeNo(outTradeNo);
            map= wxPayService.wxSpPay(wxShareOrders.getOutTradeNo(),0.01, user.getOpenId());
        }
        if (sub.equalsIgnoreCase("GOU")) {
            //支付共享订单
            WxGoodsOrders wxGoodsOrders = wxGoodsOrdersService.selectByOutTradeNo(outTradeNo);
            map= wxPayService.wxSpPay(wxGoodsOrders.getOutTradeNo(),0.01, user.getOpenId());
        }
        return ResultView.ok(map);
    }

    /**
     * @api {POST} /pay/zfb/notify 支付宝支付后回调方法
     * @apiGroup Pay
     * @apiVersion 1.0.0
     * @apiExample {url} 接口示例
     * curl -i http://120.79.18.21:8080/goodOrders/nlg/zfb/notify
     * @apiSuccessExample {json} 微信成功的响应
     * HTTP/1.1 200 OK
     * @apiSuccessExample {json} 成功的响应
     * HTTP/1.1 200 OK
     * ｛
     * "code":1
     * "msg":"操作成功"
     * ｝
     */
    @RequestMapping(value = "/zfb/notify")
    public String zfbNotify(HttpServletRequest request) {
        try {
            Map<String, String> map = XMLUtil.AliPayNotify(request);
            if (map.get("trade_status").equals("TRADE_SUCCESS")) {
                String outTradeNo = map.get("out_trade_no");
                System.out.println("支付宝回调订单号－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－" + outTradeNo);
                pay(outTradeNo,null);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("---------------------------回调通知异常！！！-------------------------------");
            return "fail";
        }
        return "fail";
    }

    /**
     * 微信支付后回调方法
     * @param request
     * @return
     */
    @PostMapping(value = "/wcPay/notify")
    public String wcPayNotify(HttpServletRequest request) {
        try {
            Map<String, String> map = XMLUtil.wxPayNotify(request);
            // 验证签名
            if ("SUCCESS".equals(map.get("return_code"))) {
                //调起支付所传入的支付流水号
                String outTradeNo = map.get("out_trade_no");
                System.out.println("微信回调订单号－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－" + outTradeNo);
                pay(outTradeNo,ResultStatus.PAYTYPE_WX);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("---------------------------回调通知异常！！！-------------------------------");
        } finally {
            return XMLUtil.setXml("SUCCESS", "OK");
        }
    }


    /**
     * 支付回调通用业务逻辑
     * @param outTradeNo 支付流水号
     * @param payType 支付类型
     */
    private void pay(String outTradeNo,Integer payType){
        String sub = outTradeNo.substring(0, 3);
        //押金支付
        if (sub.equalsIgnoreCase("BAI")) {
            wxFinanceService.payCallback(outTradeNo, payType);
        }  //共享订单支付
        else if (sub.equalsIgnoreCase("SHA")) {
            wxShareOrdersService.payCallback(outTradeNo, payType);
        }
        else if (sub.equalsIgnoreCase("GOU")){
            log.info("执行购买订单回调方法");
            wxGoodsOrdersService.callback(outTradeNo,payType);
        }
    }

    @PostMapping(value = "/getout")
    public void  test(String outTradeNo,Integer payType){
        String sub = outTradeNo.substring(0, 3);
        if (sub.equalsIgnoreCase("SHA")) {
            wxShareOrdersService.payCallback(outTradeNo, payType);
        }
        if (sub.equalsIgnoreCase("BAI")) {
            wxFinanceService.payCallback(outTradeNo, payType);
        }
    }
}
