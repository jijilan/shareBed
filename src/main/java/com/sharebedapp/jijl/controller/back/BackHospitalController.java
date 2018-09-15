package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.wrap.HospitalEquipmentInfo;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxEquipmentService;
import com.sharebedapp.jijl.service.WxHospitalService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: jijl
 * @Date 2018/8/22 14:49
 */

@RestController
@RequestMapping("/back")
public class BackHospitalController {

    private final WxHospitalService wxHospitalService;

    private final WxEquipmentService wxEquipmentService;

    @Autowired
    public BackHospitalController(WxHospitalService wxHospitalService, WxEquipmentService wxEquipmentService) {
        this.wxHospitalService = wxHospitalService;
        this.wxEquipmentService = wxEquipmentService;
    }

    @GetMapping("/getHospitalList")
    public ResultView getHospitalList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      String hospitalName , String contacts){
        PageInfo pageInfo = wxHospitalService.getHospitalList(pageNo,pageSize,hospitalName,contacts);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"hospitalList"));
    }

    @GetMapping("/getHospitalInfo")
    public ResultView getHospitalInfo(String hospitalId){
        if (StringUtils.isEmpty(hospitalId)){
            return ResultView.error(ResultEnum.CODE_21);
        }
        WxHospital wxHospital = wxHospitalService.getHospitalInfo(hospitalId);
        return ResultView.ok(wxHospital);
    }

    /**
     *
     * @param wxHospital 医院信息
     * @param recharge 收费标准
     * @return
     */
    @PostMapping("/addHospital")
    public ResultView addHospital(WxHospital wxHospital,String recharge){
        if (StringUtils.isEmpty(wxHospital.getContacts())){
            return ResultView.error(ResultEnum.CODE_33);
        }
        if (StringUtils.isEmpty(wxHospital.getContactsPhone())){
            return ResultView.error(ResultEnum.CODE_34);
        }
        if (StringUtils.isEmpty(wxHospital.getHospitalName())){
            return ResultView.error(ResultEnum.CODE_32);
        }
        if (StringUtils.isEmpty(wxHospital.getHospitalAddress())){
            return ResultView.error(ResultEnum.CODE_35);
        }
        if (StringUtils.isEmpty(wxHospital.getEquipmentPrice())){
            return ResultView.error(ResultEnum.CODE_36);
        }
        if (wxHospital.getHospitalCommissionRate() + wxHospital.getPurchaserCommissionRate()>=100){
            return ResultView.error(ResultEnum.CODE_55);
        }

        int flag = wxHospitalService.addHospital(wxHospital, recharge);

        if (flag>0){
            return ResultView.ok();
        }else if (flag == ResultStatus.INTERVAL_REPEAT){
            return ResultView.error(ResultEnum.CODE_68);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getHospitalNameList")
    public ResultView getHospitalNameList(){
        return ResultView.ok(wxHospitalService.getHospitalNameList());
    }

    @PostMapping("/bindEquipment")
    public ResultView bindEquipment(WxEquipment wxEquipment){
        String equipmentNumber = wxEquipment.getEquipmentNumber();
        WxEquipment equipmentByEqNumber= wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
        if(equipmentByEqNumber != null && equipmentByEqNumber.getIsBinding() == 1){
            wxEquipment.setEquipmentId(equipmentByEqNumber.getEquipmentId());
        }else{
            return  ResultView.error(ResultEnum.CODE_187);
        }
        int flag = wxEquipmentService.bindEquipment(wxEquipment);
        if (flag>0){
            return ResultView.ok();
        }else if (flag == ResultStatus.BINDED){
            return ResultView.error(ResultEnum.CODE_66);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @PostMapping("/updateHospitalStatus")
    public ResultView updateHospitalStatus(String hospitalId, Integer status){
        int flag = wxHospitalService.forbidHospital(hospitalId, status);
        if (flag>0){
            return ResultView.ok();
        }else if (flag == -1){
            return ResultView.error(ResultEnum.CODE_56);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getEquipmentByHospitalId")
    public ResultView getEquipmentByHospitalId(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                               String hospitalId ,String categoryId ,String department ,String ward ){

        if (hospitalId == null){
            return ResultView.error(ResultEnum.CODE_21);
        }
        PageInfo<HospitalEquipmentInfo> pageInfo = wxEquipmentService.getEquipmentListByHospitalId(pageNo,pageSize,hospitalId,categoryId,department,ward);

        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"wxEquipmentList"));
    }

    @PostMapping("/unbindEquipment")
    public ResultView unbindEquipment(String equipmentNumber){
        if (wxEquipmentService.unbindEquipment(equipmentNumber)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
