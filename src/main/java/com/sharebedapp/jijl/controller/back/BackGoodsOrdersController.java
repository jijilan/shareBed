package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxGoodsOrdersService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/back")
public class BackGoodsOrdersController {



    private final WxGoodsOrdersService wxGoodsOrdersService;

    @Autowired
    public BackGoodsOrdersController(WxGoodsOrdersService wxGoodsOrdersService) {
        this.wxGoodsOrdersService = wxGoodsOrdersService;
    }

    /***
     * 设备订单记录
     * @param goodsOrdersId 订单编号
     *  @param userPhone 手机号
     * @param categoryId 设备类型
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getGoodsOrdersList")
    public ResultView getGoodsOrdersList(String goodsOrdersId, String userPhone, String categoryId, @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                          @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return wxGoodsOrdersService.getGoodsOrdersList(goodsOrdersId,userPhone,categoryId,pageNo,pageSize);
    }

    /***
     * 设备订单详情
     * @param goodsOrdersId 订单编号
     * @return
     */
    @GetMapping("/getGoodsOrders")
    public ResultView getGoodsOrders(String goodsOrdersId){
        if(StringUtil.isEmpty(goodsOrdersId)){
            return  ResultView.error(ResultEnum.CODE_205);
        }
        return wxGoodsOrdersService.getGoodsOrders(goodsOrdersId);
    }
}
