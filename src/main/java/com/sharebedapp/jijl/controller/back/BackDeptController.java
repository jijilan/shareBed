package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxDept;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxDeptService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 11:06
 */
@RestController
@RequestMapping("/back")
public class BackDeptController {

    private final WxDeptService wxDeptService;

    @Autowired
    public BackDeptController(WxDeptService wxDeptService) {
        this.wxDeptService = wxDeptService;
    }

    @PostMapping("/addDept")
    public ResultView addDept(WxDept wxDept){
        if (StringUtils.isEmpty(wxDept.getDeptName())){
            return ResultView.error(ResultEnum.CODE_27);
        }
        if (wxDept.getDeptNumber() == null){
            return ResultView.error(ResultEnum.CODE_28);
        }
        if (wxDept.getFid() == null){
            return ResultView.error(ResultEnum.CODE_29);
        }
        if (wxDeptService.addDept(wxDept)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @DeleteMapping("/deleteDept")
    public ResultView deleteDept(Integer deptId){
        if (deptId==null){
            return ResultView.error(ResultEnum.CODE_26);
        }
        if (wxDeptService.deleteDept(deptId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getDeptList")
    public ResultView getDeptList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  WxDept wxDept , Integer deptType){
        if (deptType != ResultStatus.DEPT_TYPE_DEPART && deptType != ResultStatus.DEPT_TYPE_WARD && deptType != ResultStatus.DEPT_TYPE_BEDNUMBER){
            return ResultView.error(ResultEnum.CODE_57);
        }
        PageInfo pageInfo = wxDeptService.getDeptList(pageNo,pageSize,wxDept,deptType);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"deptList"));
    }

    @GetMapping("/getDeptNameList")
    public ResultView getDeptNameList(WxDept wxDept){
        if (wxDept.getFid()==null){
            return ResultView.error(ResultEnum.CODE_29);
        }
        List<WxDept> wxDeptList = wxDeptService.getDeptNameList(wxDept);
        return ResultView.ok(wxDeptList);
    }
}
