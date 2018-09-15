package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.mapper.WxSystemMapper;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCashRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jijl
 * @Date 2018/8/28 11:21
 */
@RestController
@RequestMapping("/front")
public class FrontCashRequest {

    @Autowired
    private WxCashRequestService wxCashRequestService;
    @Autowired
    private WxSystemMapper wxSystemMapper;
    @PostMapping("/withdrawDeposit")
    public ResultView withdrawDeposit(HttpServletRequest request, String bankCardId, double amount){
        String consumerId;
        if (request.getAttribute(ResultStatus.USER_ID)!= null) {
            consumerId = (String) request.getAttribute(ResultStatus.USER_ID);
        }else {
            consumerId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        }
        if (consumerId == null){
            return ResultView.error(ResultEnum.CODE_2);
        }
        if (amount <= 0){
            return ResultView.error(ResultEnum.CODE_60);
        }
        if (amount < Double.valueOf(wxSystemMapper.getSystem(ResultStatus.SYSTEMTYPE_WITHDRAW).getParameter())){
            return ResultView.error(ResultEnum.CODE_63);
        }
        int flag = wxCashRequestService.withdrawDeposit(consumerId, bankCardId, amount);
        if (flag>0){
            return ResultView.ok();
        }else if (flag == ResultStatus.WITHDRAW_AMOUNT){
            return ResultView.error(ResultEnum.CODE_61);
        }else if (flag == ResultStatus.WITHDRAW_COUNT){
            return ResultView.error(ResultEnum.CODE_64);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
