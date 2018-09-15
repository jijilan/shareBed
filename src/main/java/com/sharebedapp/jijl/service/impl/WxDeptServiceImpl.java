package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxDeptMapper;
import com.sharebedapp.jijl.model.WxDept;
import com.sharebedapp.jijl.model.wrap.SickBedInfo;
import com.sharebedapp.jijl.model.wrap.WardInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.service.WxDeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 11:13
 */

@Service
public class WxDeptServiceImpl implements WxDeptService {

    @Autowired
    private WxDeptMapper wxDeptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addDept(WxDept wxDept) {
        wxDept.setcTime(new Date());
        return wxDeptMapper.insertSelective(wxDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDept(Integer deptId) {
        return wxDeptMapper.deleteByPrimaryKey(deptId);
    }

    @Override
    public PageInfo getDeptList(Integer pageNo, Integer pageSize, WxDept wxDept, Integer deptType) {
        PageHelper.startPage(pageNo,pageSize);
        if (deptType== ResultStatus.DEPT_TYPE_DEPART){
            return new PageInfo(wxDeptMapper.selectByExample(wxDept),pageSize);
        }else if (deptType== ResultStatus.DEPT_TYPE_WARD){
            /*WxDept dept = new WxDept();
            if (wxDept.getDeptId()==null) {
                dept.setFid(0);
            }else {
                dept.setDeptId(wxDept.getDeptId());
            }
            dept.setHospitalId(wxDept.getHospitalId());*/
            List<WxDept> wxDepts = wxDeptMapper.selectByExample(wxDept);
            List<WardInfo> wardInfoList = new LinkedList<>();
            for (WxDept depart : wxDepts) {
                WxDept ward = new WxDept();
                ward.setFid(depart.getDeptId());
                List<WxDept> wardList = wxDeptMapper.selectByExample(ward);
                for (WxDept wxWard : wardList) {
                    WardInfo wardInfo = new WardInfo();
                    wardInfo.setDeptId(wxWard.getDeptId());
                    wardInfo.setBuilding(depart.getDeptNumber());
                    wardInfo.setDepart(depart.getDeptName());
                    wardInfo.setFloor(wxWard.getDeptNumber());
                    wardInfo.setWard(wxWard.getDeptName());
                    wardInfoList.add(wardInfo);
                }
            }
            return new PageInfo(wardInfoList,pageSize);
        }else{
            if (wxDept.getFid() == 0 && wxDept.getDeptId() == null){
                List<WxDept> wxWardList = new LinkedList<>();
                //查询所有的科室
                List<WxDept> departs = wxDeptMapper.selectByExample(wxDept);
                for (WxDept depart : departs) {
                    WxDept wxWard = new WxDept();
                    wxWard.setFid(depart.getDeptId());
                    wxWard.setHospitalId(depart.getHospitalId());
                    List<WxDept> wxDeptList = wxDeptMapper.selectByExample(wxWard);
                    for (WxDept dept : wxDeptList) {
                        wxWardList.add(dept);
                    }
                }
                return new PageInfo(getSickBedInfoListByWardList(wxWardList),pageSize);
            }
            else if (wxDept.getDeptId() != null && wxDeptMapper.selectByPrimaryKey(wxDept.getDeptId()).getFid() == 0){
                return new PageInfo(getSickBedInfoListByWardList(wxDeptMapper.selectByExample(wxDept)),pageSize);
            }else {
                return new PageInfo(getSickBedInfoByWard(wxDept),pageSize);
            }
        }
    }

    @Override
    public List<WxDept> getDeptNameList(WxDept wxDept) {
        return wxDeptMapper.selectByExample(wxDept);
    }

    private List<SickBedInfo> getSickBedInfoListByWardList(List<WxDept> wxWards){
        List<SickBedInfo> sickBedInfoList = new LinkedList<>();
        for (WxDept wxWard : wxWards) {
            List<SickBedInfo> sickBedInfoByWard = getSickBedInfoByWard(wxWard);
            for (SickBedInfo sickBedInfo : sickBedInfoByWard) {
                sickBedInfoList.add(sickBedInfo);
            }
        }
        return sickBedInfoList;
    }

    private List<SickBedInfo> getSickBedInfoByWard(WxDept wxDept){
        List<SickBedInfo> sickBedInfoList = new LinkedList<>();
        WxDept wxWard = wxDeptMapper.selectByPrimaryKey(wxDept.getDeptId());
        List<WxDept> sickBedList = wxDeptMapper.selectBySickBedByWard(wxDept.getDeptId());

        for (WxDept dept : sickBedList) {
            SickBedInfo sickBedInfo = new SickBedInfo();
            sickBedInfo.setDeptId(dept.getDeptId());
            sickBedInfo.setBedNumber(dept.getDeptName());
            sickBedInfo.setWard(wxWard.getDeptName());
            sickBedInfoList.add(sickBedInfo);
        }
        return sickBedInfoList;
    }
}
