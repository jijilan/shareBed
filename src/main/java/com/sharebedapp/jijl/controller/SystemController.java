package com.sharebedapp.jijl.controller;

import com.sharebedapp.jijl.model.WxSystem;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class SystemController {

    @Autowired
    private WxSystemService wxSystemService;

    /***
     * 获取系统参数
     * @param systemType  1:押金设置 2:客服电话 3.每天可提现次数 4.最低提现金额 5:提现须知 6:商户名称
     * @return
     */
    @GetMapping(value = "getSystem")
    public ResultView getSystem(Integer systemType) {
        if(systemType==null){
           return  ResultView.error(ResultEnum.CODE_130);
        }
        if(systemType!=1&&systemType!=2&&systemType!=3&&systemType!=4&&systemType!=5&&systemType!=6){
            return  ResultView.error(ResultEnum.CODE_218);
        }
         WxSystem wxSystem=wxSystemService.getSystem(systemType);
        if(wxSystem==null){
            return  ResultView.error(ResultEnum.CODE_157);
          }
        String parameter = wxSystem.getParameter();
        HashMap<String,Object> map=new HashMap<>(1);
        map.put("parameter",parameter);
        return  ResultView.ok(map);
    }

}
