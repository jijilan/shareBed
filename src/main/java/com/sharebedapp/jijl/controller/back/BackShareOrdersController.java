package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxShareOrdersService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hzm
 */
@RestController
@RequestMapping("/back")
public class BackShareOrdersController {
    private final WxShareOrdersService wxShareOrdersService;

    @Autowired
    public BackShareOrdersController(WxShareOrdersService wxShareOrdersService) {
        this.wxShareOrdersService = wxShareOrdersService;
    }

    /***
     * 共享订单记录
     * @param shareOrderId 订单编号
     *  @param userPhone 手机号
     * @param categoryId 设备类型
     * @param  hospitalName 医院名
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getShareOrdersList")
    public ResultView getShareOrderseList(String shareOrderId,String userPhone,String categoryId,String  hospitalName,@RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                       @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return wxShareOrdersService.getShareOrderseList(shareOrderId,userPhone,categoryId,hospitalName,pageNo,pageSize);
    }

    /***
     * 共享订单详情
     * @param shareOrderId 订单编号
     * @return
     */
    @GetMapping("/getShareOrders")
    public ResultView getShareOrders(String shareOrderId){
        if(StringUtil.isEmpty(shareOrderId)){
            return  ResultView.error(ResultEnum.CODE_205);
        }
        return wxShareOrdersService.getShareOrderse(shareOrderId);
    }
}
