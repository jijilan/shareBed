package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxCashrequest;
import com.sharebedapp.jijl.model.wrap.CashRequest;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCashRequestService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/back")
public class BackCashrequestController {

    private final WxCashRequestService wxCashRequestService;

    @Autowired
    public BackCashrequestController(WxCashRequestService wxCashRequestService) {
        this.wxCashRequestService = wxCashRequestService;
    }

    /***
     * 提现记录
     *  @param userPhone 账号
     * @param  phoneNumber 手机号
     *  @param bankRealName 名字
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getCashRequestList")
    public ResultView getCashRequestList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     String userPhone, String phoneNumber , String bankRealName){
        return wxCashRequestService.getCashRequestList(pageNo,pageSize,userPhone,phoneNumber,bankRealName);
    }

    /***
     * 提现通过，驳回
      * @param  cashRequestId 提现编号
     * @param  status 1.审核中 2.通过 3.驳回
     * @param  nayReason  驳回理由
     * @return
     */
    @PostMapping("/updCashRequest")
    public ResultView updCashRequest(String cashRequestId,String nayReason, Integer status){
        if(StringUtil.isEmpty(cashRequestId)){
            return ResultView.error(ResultEnum.CODE_143);
        }
        if(status==null){
            return ResultView.error(ResultEnum.CODE_130);
        }
        WxCashrequest wxCashrequest= wxCashRequestService.getBycashRequestId(cashRequestId);
        if(wxCashrequest!=null){
        if(status==ResultStatus.CASHREQUEST_STATUS_Y){
            wxCashrequest.setStatus(status);
            wxCashrequest.setuTime(new Date());
        }
         if(status==ResultStatus.CASHREQUEST_STATUS_N){
            if(StringUtil.isEmpty(nayReason)){
                return  ResultView.error(ResultEnum.CODE_145);
             }
                wxCashrequest.setStatus(status);
                wxCashrequest.setNayReason(nayReason);
                wxCashrequest.setuTime(new Date());
            }
        return wxCashRequestService.updCashRequest(wxCashrequest)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);

         }
        return  ResultView.error(ResultEnum.CODE_1002);
    }

    @GetMapping("/getCashRequest")
    public ResultView getCashRequest(){
        CashRequest cashRequest = wxCashRequestService.getCashRequest();
        return ResultView.ok(cashRequest);
    }

}
