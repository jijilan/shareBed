package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxAgent;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxAgentService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: jijl
 * @Date 2018/8/23 17:33
 */

@RestController
@RequestMapping("/back")
public class BackAgentController {

    private final WxAgentService wxAgentService;

    @Autowired
    public BackAgentController(WxAgentService wxAgentService) {
        this.wxAgentService = wxAgentService;
    }

    @PostMapping("/addAgent")
    public ResultView addAgent(WxAgent wxAgent){
        if (StringUtils.isEmpty(wxAgent.getAgentAccount())){
            return ResultView.error(ResultEnum.CODE_41);
        }
        if (StringUtils.isEmpty(wxAgent.getAgentPassWord())){
            return ResultView.error(ResultEnum.CODE_42);
        }
        if (StringUtils.isEmpty(wxAgent.getAgentNickName())){
            return ResultView.error(ResultEnum.CODE_43);
        }
        if (StringUtils.isEmpty(wxAgent.getAgentPhone())){
            return ResultView.error(ResultEnum.CODE_45);
        }
        if (StringUtils.isEmpty(wxAgent.getHospitalId())){
            return ResultView.error(ResultEnum.CODE_44);
        }
        int flag = wxAgentService.addAgent(wxAgent);
        if (flag>0){
            return ResultView.ok();
        }else if (flag == ResultStatus.AGENT_EXISTED){
            return ResultView.error(ResultEnum.CODE_58);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getAgentList")
    public ResultView getAgentList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                   String roleName,String agentAccount,String agentNickName,Integer proportion,String agentPhone, String hospitalId ){
        PageInfo pageInfo = wxAgentService.getAgentList(pageNo,pageSize,roleName,agentAccount,agentNickName,proportion,agentPhone,hospitalId);
        if (pageInfo!=null){
            return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"agentList"));
        }
        return ResultView.error(ResultEnum.CODE_2);
    }



    @PostMapping("/updateAgent")
    public ResultView updateAgent(WxAgent wxAgent){
        if (StringUtils.isEmpty(wxAgent.getAgentId())){
            return ResultView.error(ResultEnum.CODE_53);
        }
        int flag = wxAgentService.updateAgent(wxAgent);
        if (flag > 0){
            return ResultView.ok();
        }else if (flag == ResultStatus.PROPORTION_OVER){
            return ResultView.error(ResultEnum.CODE_67);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @PostMapping("/updateAgentStatus")
    public ResultView updateAgentStatus(String agentId , Integer status){

        if (wxAgentService.updateAgentStatus(agentId,status)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
