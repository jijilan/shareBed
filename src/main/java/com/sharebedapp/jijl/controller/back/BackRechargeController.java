package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.wrap.RechargeInfo;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: jijl
 * @date 2018/8/31 0031 11:07
 */
@RestController
@RequestMapping("/back")
public class BackRechargeController {

    private final WxRechargeService wxRechargeService;

    @Autowired
    public BackRechargeController(WxRechargeService wxRechargeService) {
        this.wxRechargeService = wxRechargeService;
    }

    @GetMapping("/getRechargeList")
    public ResultView getRechargeList(String hospitalId){
        List<RechargeInfo> rechargeInfoList = wxRechargeService.getRechargeList(hospitalId);
        if (rechargeInfoList.size()>0) {
            return ResultView.ok(rechargeInfoList);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
