package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxMessage;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxMessageService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: jijl
 * @Date 2018/8/23 16:34
 */
@RestController
@RequestMapping("/back")
public class BackMessageController {
    private final WxMessageService wxMessageService;

    @Autowired
    public BackMessageController(WxMessageService wxMessageService) {
        this.wxMessageService = wxMessageService;
    }

    @PostMapping("/addMessage")
    public ResultView addMessage(WxMessage wxMessage){
        if (StringUtils.isEmpty(wxMessage.getTitle())){
            return ResultView.error(ResultEnum.CODE_37);
        }
        if (StringUtils.isEmpty(wxMessage.getContext())){
            return ResultView.error(ResultEnum.CODE_38);
        }
        if (wxMessageService.addMessage(wxMessage)>0){
         return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getMessageList")
    public ResultView getMessageList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     String title, Long startTime,Long endTime){
        PageInfo pageInfo = wxMessageService.getMessageList(pageNo,pageSize,title,startTime,endTime);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"messageList"));
    }

    @GetMapping("/getMessageInfo")
    public ResultView getMessageInfo(String messageId){
        if (StringUtils.isEmpty(messageId)){
            return ResultView.error(ResultEnum.CODE_39);
        }
        return ResultView.ok(wxMessageService.getMessageInfo(null, messageId));
    }

    @DeleteMapping("/deleteMessage")
    public ResultView deleteMessage(String messageId){
        if (messageId == null){
            return ResultView.error(ResultEnum.CODE_39);
        }
        if (wxMessageService.deleteById(messageId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
