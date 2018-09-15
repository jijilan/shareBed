package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxAftersale;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxAftersaleService;
import com.sharebedapp.jijl.service.WxUserService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.UploadFileUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/back")
public class BackAftersaleController {

    @Value("${web.portrait-path}")
    private String portraitPath;
    @Autowired
    private WxAftersaleService wxAftersaleService;
    @Autowired
    private WxUserService wxUserService;
    /***
     * 维修记录
     * @param equipmentId 设备id
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getAftersaleList")
    public ResultView getAfterSaleList(String equipmentId,
                                       @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                      @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        if(StringUtil.isEmpty(equipmentId)){
            return  ResultView.error(ResultEnum.CODE_203);
        }
        return wxAftersaleService.getAftersaleList(equipmentId,pageNo,pageSize);
    }

    /***
     * 删除维修记录
     * @param afterSaleId 维修记录id
     * @return
     */
    @PostMapping("/delAftersale")
    public ResultView delAfterSale(String afterSaleId){
        if(StringUtil.isEmpty(afterSaleId)){
            return  ResultView.error(ResultEnum.CODE_164);
        }
        return wxAftersaleService.delAftersale(afterSaleId)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);
    }


    /***
     * 新增维修记录
     * @param equipmentId 设备id
     * @param afterSaleType 售后类型【1.保洁 2.报修】
     * @param userPhone 手机号
     * @param price 金额
     * @param file 图片
     * @return
     */
    @PostMapping("/addAftersale")
    public ResultView addAfterSale(String equipmentId,Integer afterSaleType,String userPhone,Double price,@RequestParam(value =
            "file",required = false) MultipartFile[] file){
        if(StringUtil.isEmpty(equipmentId)){
            return  ResultView.error(ResultEnum.CODE_203);
        }
        if(afterSaleType==null){
            return  ResultView.error(ResultEnum.CODE_131);
        }
        if(afterSaleType!=1&&afterSaleType!=2){
            return  ResultView.error(ResultEnum.CODE_415);
        }
        if(userPhone==null){
            return  ResultView.error(ResultEnum.CODE_9);
        }
        WxUser user = wxUserService.getByUserPhone(userPhone);
        if(user==null){
            return  ResultView.error(ResultEnum.CODE_163);
        }
        if(price==null){
            return  ResultView.error(ResultEnum.CODE_165);
        }
        WxAftersale wxAftersale=new WxAftersale();
        wxAftersale.setAfterSaleId(IdentityUtil.identityId("AF"));
        wxAftersale.setAfterSaleType(afterSaleType);
        wxAftersale.setEquipmentId(equipmentId);
        wxAftersale.setUserId(user.getUserId());
        wxAftersale.setPrice(BigDecimal.valueOf(price));
        wxAftersale.setAfterSalePrice(UploadFileUtil.flowUpload(file,portraitPath ,ResultStatus.AFTERSALE_IMG_PATH));
        wxAftersale.setcTime(new Date());
        return wxAftersaleService.addAftersale(wxAftersale)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);
    }
}
