package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.wrap.RevenueInfo;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFinanceService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jijl
 * @Date 2018/8/24 18:13
 */
@RestController
@RequestMapping("/back")
public class BackFinanceController {

    private final WxFinanceService wxFinanceService;

    @Autowired
    public BackFinanceController(WxFinanceService wxFinanceService) {
        this.wxFinanceService = wxFinanceService;
    }

    @GetMapping("/getFinanceList")
    public ResultView getFinanceList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     String hospitalName,String categoryId ,Integer financeType){

        PageInfo pageInfo = wxFinanceService.getFinanceList(pageNo,pageSize,hospitalName,categoryId,financeType);

        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"financeList"));
    }


    /***
     * 押金退还列表
     * @param userPhone 手机号
      * @param nickName 昵称
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping(value = "/getFinanceBailList")
    public ResultView getFinanceBailList(String userPhone,String nickName, @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                          @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
     return  wxFinanceService.getFinanceBailList(userPhone,nickName,pageNo,pageSize);
    }

    @GetMapping("/getRevenueInfo")
    public ResultView getRevenueInfo(){
        RevenueInfo revenueInfo = wxFinanceService.getRevenueInfo();
        return ResultView.ok(revenueInfo);
    }
}
