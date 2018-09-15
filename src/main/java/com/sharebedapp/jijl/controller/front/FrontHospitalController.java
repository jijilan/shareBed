package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.wrap.HospitalEquipment;
import com.sharebedapp.jijl.model.wrap.HospitalInfo;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCategoryService;
import com.sharebedapp.jijl.service.WxHospitalService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 20:21
 */
@RestController
@RequestMapping("front")
public class FrontHospitalController {

    @Autowired
    private WxHospitalService wxHospitalService;

    @Autowired
    private WxCategoryService wxCategoryService;

    @GetMapping("/getHospitalInfoList")
    public ResultView getHospitalInfoList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                          String hospitalProvince ,String hospitalCity ,String hospitalArea ,String hospitalName){
        PageInfo pageInfo = wxHospitalService.getHospitalInfoList(pageNo,pageSize,hospitalProvince,hospitalCity, hospitalArea ,hospitalName);

        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"hospitalInfoList"));
    }

    @GetMapping("/getHospitalList")
    public ResultView getHospitalList(){
        List<HospitalInfo> hospitalNameList = wxHospitalService.getHospitalNameList();
        return ResultView.ok(hospitalNameList);
    }

    @GetMapping("/getHospitalInfo")
    public ResultView getHospitalInfo(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      String hospitalId){
        HospitalEquipment hospitalEquipment = wxHospitalService.getHospitalEquipment(pageNo,pageSize,hospitalId);
        return ResultView.ok(hospitalEquipment);
    }
}
