package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.WxBankcards;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxBankcardsService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jijl
 * @Date 2018/8/23 22:02
 */

@RestController
@RequestMapping("/front")
public class FrontBankCardsController {
    @Autowired
    private WxBankcardsService wxBankcardsService;

    @PostMapping("/addBankcars")
    public ResultView addBankcars(HttpServletRequest request,WxBankcards wxBankcards){
        String consumerId;
        if (request.getAttribute(ResultStatus.AGENT_ID)!=null){
            consumerId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        }else {
            consumerId = (String) request.getAttribute(ResultStatus.USER_ID);
        }

        if (StringUtils.isEmpty(wxBankcards.getBankNumber())){
            return ResultView.error(ResultEnum.CODE_47);
        }
        if (StringUtils.isEmpty(wxBankcards.getBankRealName())){
            return ResultView.error(ResultEnum.CODE_48);
        }
        if (StringUtils.isEmpty(wxBankcards.getPhoneNumber())){
            return ResultView.error(ResultEnum.CODE_49);
        }
        if (StringUtils.isEmpty(wxBankcards.getBankName())){
            return ResultView.error(ResultEnum.CODE_50);
        }
        if (StringUtils.isEmpty(wxBankcards.getBankCardType())){
            return ResultView.error(ResultEnum.CODE_51);
        }
        if (wxBankcardsService.addBankcards(wxBankcards,consumerId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @DeleteMapping("/deletebankCards")
    public ResultView deletebankCards(String bankCardId){
        if (StringUtils.isEmpty(bankCardId)){
            return ResultView.error(ResultEnum.CODE_52);
        }
        if (wxBankcardsService.deletebankCards(bankCardId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getBankcardsList")
    public ResultView getBankcardsList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                       HttpServletRequest request){
        String consumerId;
        if (request.getAttribute(ResultStatus.USER_ID) != null){
            consumerId = (String) request.getAttribute(ResultStatus.USER_ID);
        }else {
            consumerId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        }
        PageInfo pageInfo = wxBankcardsService.getBankcardsList(pageNo,pageSize,consumerId);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"bankcardsList"));
    }
}
