package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.wrap.UserFinance;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jijl
 * @Date 2018/8/24 18:13
 */

@RestController
@RequestMapping("/front")
public class FrontFinanceController {

    @Autowired
    private WxFinanceService wxFinanceService;

    @GetMapping("/getFinanceByUser")
    public ResultView getFinanceByUser(HttpServletRequest request, Integer days, String startTime ,String endTime ,String hospitalId){
        String userId = null;
        String agentId = null;
        if ( request.getAttribute(ResultStatus.USER_ID) != null){
            userId = (String) request.getAttribute(ResultStatus.USER_ID);
        }else {
            agentId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        }
        UserFinance userFinance = wxFinanceService.getFinanceByUser(userId,agentId,days,startTime,endTime,hospitalId);
        if (userFinance == null){
            return ResultView.error(ResultEnum.CODE_2);
        }
        return ResultView.ok(userFinance);
    }


}
