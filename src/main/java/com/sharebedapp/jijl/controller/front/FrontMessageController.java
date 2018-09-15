package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.WxMessage;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxMessageService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jijl
 * @Date 2018/8/23 20:05
 */
@RestController
@RequestMapping("/front")
public class FrontMessageController {

    @Autowired
    private WxMessageService wxMessageService;

    @GetMapping("/getMessageList")
    public ResultView getMessageList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        PageInfo messageList = wxMessageService.getMessageList(pageNo, pageSize, null,null,null);
        return ResultView.ok(JsonUtils.PageInfoToMap(messageList,"messageList"));
    }

    @GetMapping("/getMessageInfo")
    public ResultView getMessageInfo(HttpServletRequest request,String messageId){
        String consumerId ;
        if ( request.getAttribute(ResultStatus.USER_ID) != null){
            consumerId = (String) request.getAttribute(ResultStatus.USER_ID);
        }else {
            consumerId = (String) request.getAttribute(ResultStatus.AGENT_ID);
        }
        if (StringUtils.isEmpty(messageId)){
            return ResultView.error(ResultEnum.CODE_39);
        }
        WxMessage messageInfo = wxMessageService.getMessageInfo(consumerId,messageId);
        return ResultView.ok(messageInfo);
    }
}
