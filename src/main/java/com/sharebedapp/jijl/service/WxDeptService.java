package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxDept;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 11:13
 */
public interface WxDeptService {
    int addDept(WxDept wxDept);

    int deleteDept(Integer deptId);

    PageInfo getDeptList(Integer pageNo, Integer pageSize, WxDept wxDept, Integer deptType);

    List<WxDept> getDeptNameList(WxDept wxDept);
}
