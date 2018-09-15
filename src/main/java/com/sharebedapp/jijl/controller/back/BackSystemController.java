package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxSystem;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: jijl
 * @date 2018/8/23 17:12
 */
@RestController
@RequestMapping("/back")
public class BackSystemController {

    private final WxSystemService wxSystemService;

    @Autowired
    public BackSystemController(WxSystemService wxSystemService) {
        this.wxSystemService = wxSystemService;
    }

    @GetMapping("/getSystemParamter")
    public ResultView getSystemParamter(WxSystem wxSystem){
        if (wxSystem.getSystemType()==null){
            return ResultView.error(ResultEnum.CODE_40);
        }
        WxSystem System = wxSystemService.getSystem(wxSystem.getSystemType());
        return ResultView.ok(System);
    }

    @PostMapping("/addSystemParamter")
    public ResultView addSystemParamter(WxSystem wxSystem){
        if (wxSystemService.addSystemParamter(wxSystem)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @PutMapping("/updateSystemParamter")
    public ResultView updateSystemParamter(WxSystem wxSystem){
        if (wxSystem.getSystemType()==null){
            return ResultView.error(ResultEnum.CODE_40);
        }
        //设置 默认押金至少为1
        if (wxSystem.getSystemType()==ResultStatus.SYSTEMTYPE_1){
            String parameter = wxSystem.getParameter();
            if("0".equals(parameter)){
              return  ResultView.error(ResultEnum.CODE_184);
            }
        }
        if (wxSystemService.updateSystemParamter(wxSystem)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
