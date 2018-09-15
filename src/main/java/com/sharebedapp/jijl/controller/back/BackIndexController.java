package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.IndexService;
import com.sharebedapp.jijl.service.WxCategoryService;
import com.sharebedapp.jijl.service.WxGoodsOrdersService;
import com.sharebedapp.jijl.service.WxShareOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

/**
 * @author hzm
 */
@RestController
@RequestMapping("/back")
public class  BackIndexController {

    private final WxShareOrdersService wxShareOrdersService;
    private final WxGoodsOrdersService wxGoodsOrdersService;
    private final IndexService indexService;

    @Autowired
    public BackIndexController(WxShareOrdersService wxShareOrdersService, WxGoodsOrdersService wxGoodsOrdersService, IndexService indexService, WxCategoryService wxCategoryService) {
        this.wxShareOrdersService = wxShareOrdersService;
        this.wxGoodsOrdersService = wxGoodsOrdersService;
        this.indexService = indexService;
    }

    /**
     *首页
     * @param categoryId 设备类型Id
     * @return
     */
    @GetMapping(value = "/getIndexDetail")
    public ResultView getIndexDetail(String categoryId) {
            //共享订单量
            int shareOrderCount = wxShareOrdersService.getShareOrderCount(categoryId);
            //共享日收入
            int todayAccount = wxShareOrdersService.getTodayAccount(categoryId);
            //共享月收入
            int monthAccount = wxShareOrdersService.getMonthAccount(categoryId);
            //销售订单量
            int goodsOrderCount = wxGoodsOrdersService.getGoodsOrderCount(categoryId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shareOrderCount", shareOrderCount);
            map.put("todayAccount", todayAccount);
            map.put("monthAccount", monthAccount);
            map.put("goodsOrderCount", goodsOrderCount);

        return ResultView.ok(map);
    }
    /**
     * 首页统计折线图（后台）
     * @param orderType  1.共享  2. 销售
     * @param type  1.一周  2. 一个月
     * @return
     */
    @GetMapping(value="/getSevenToMonth")
    public ResultView getSevenToMonth(Integer orderType,Integer type){
        if(type == null || (type != 1 && type != 2)){
            return ResultView.error(ResultEnum.CODE_415);
        }
        if(orderType == null || (orderType != 1 && orderType != 2)){
            return ResultView.error(ResultEnum.CODE_415);
        }
        ResultView resultView = indexService.getSevenToMonth(type,orderType);
        return resultView;
    }
}
