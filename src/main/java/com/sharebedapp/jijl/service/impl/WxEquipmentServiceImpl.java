package com.sharebedapp.jijl.service.impl;

import com.alibaba.fastjson.JSON;
import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.WxAftersale;
import com.sharebedapp.jijl.model.WxDept;
import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxShareOrders;
import com.sharebedapp.jijl.model.wrap.EquipmentDetail;
import com.sharebedapp.jijl.model.wrap.EquipmentRecord;
import com.sharebedapp.jijl.model.wrap.HospitalEquipmentInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxEquipmentService;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WxEquipmentServiceImpl implements WxEquipmentService {

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;
    @Autowired
    private WxAftersaleMapper wxAftersaleMapper;
    @Autowired
    private WxShareOrdersMapper wxShareOrdersMapper;
    @Autowired
    private WxCategoryMapper wxCategoryMapper;
    @Autowired
    private WxDeptMapper wxDeptMapper;
    @Autowired
    WxHospitalMapper wxHospitalMapper;
    @Override
    public ResultView getEquipmentList(String equipmentNumber, String categoryId, String hospitalName, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Map<String, Object>> equipmentList = wxEquipmentMapper.getEquipmentList(equipmentNumber, categoryId, hospitalName);
        for (Map<String, Object> equipment : equipmentList) {
            String equipmentPrice = (String) equipment.get("equipmentPrice");
            String categoryIds = (String) equipment.get("categoryId");
            Map maps = (Map)JSON.parse(equipmentPrice);
           if(maps!=null){
               equipment.put("equipmentPrice", maps.get(categoryIds));
            }
        }
        PageInfo pageInfo=new PageInfo(equipmentList, pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"equipmentList"));
    }

    @Override
    public int addEquipment(WxEquipment wxEquipment) {
        return wxEquipmentMapper.insertSelective(wxEquipment);
    }

    @Override
    public WxEquipment getEquipmentByEqNumber(String equipmentNumber) {
        return wxEquipmentMapper.getEquipmentByEqNumber(equipmentNumber);
    }

    @Override
    public WxEquipment getEquipmentByEquipmentId(String equipmentId) {
        return wxEquipmentMapper.selectByPrimaryKey(equipmentId);
    }

    @Override
    public int upEquipment(WxEquipment wxEquipment) {
        return wxEquipmentMapper.updateByPrimaryKeySelective(wxEquipment);
    }

    @Override
    public ResultView getEquipment(String equipmentId) {
        HashMap<String,Object> map=new HashMap<>(1);
        Map<String, Object> equipment = wxEquipmentMapper.getEquipment(equipmentId);
        String equipmentPrice = (String) equipment.get("equipmentPrice");
        String categoryIds = (String) equipment.get("categoryId");
        Map maps = (Map)JSON.parse(equipmentPrice);
        if(maps!=null){
            equipment.put("equipmentPrice", maps.get(categoryIds));
        }
        map.put("equipment", equipment);
        return ResultView.ok(map);
    }

    @Override
    public List<String> getEquipmentNumberList() {
        return wxEquipmentMapper.getEquipmentNumberList();
    }

    //查询保洁和维修人员 共享订单的预约医院详情
    @Override
    public List<Map<String, Object>> getShareorderHospital(String equipmentNumber) {
        return wxEquipmentMapper.getShareorderHospital(equipmentNumber);
    }

    @Override
    public PageInfo<HospitalEquipmentInfo> getEquipmentListByHospitalId(Integer pageNo, Integer pageSize, String hospiatlId, String categoryId, String department, String ward) {
        PageHelper.startPage(pageNo,pageSize);

        return new PageInfo(wxEquipmentMapper.getEquipmentListByHospitalId(hospiatlId,categoryId,department,ward),pageSize);
    }

    @Override
    public boolean getByCategoryId(String categoryId) {
        List<WxEquipment> list = wxEquipmentMapper.getByCategoryId(categoryId);
        if(list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public int unbindEquipment(String equipmentNumber) {
        return wxEquipmentMapper.unbindEquipment(equipmentNumber);
    }

    @Override
    public EquipmentDetail getEquipmentInfo(String equipmentId) {
        EquipmentDetail equipmentDetail = new EquipmentDetail();
        WxEquipment wxEquipment = wxEquipmentMapper.selectByPrimaryKey(equipmentId);
        equipmentDetail.setEquipmentId(wxEquipment.getEquipmentId());

        equipmentDetail.setEquipmentNumber(wxEquipment.getEquipmentNumber());
        equipmentDetail.setCategoryName(wxCategoryMapper.selectByPrimaryKey(wxEquipment.getCategoryId()).getCategoryName());
        equipmentDetail.setHospitalName(wxHospitalMapper.selectByPrimaryKey(wxEquipment.getHospitalId()).getHospitalName());
        if (wxEquipment.getDepartmentId()!=null){
            equipmentDetail.setBuilding(wxDeptMapper.selectByPrimaryKey(wxEquipment.getDepartmentId()).getDeptNumber());
        }
        if (wxEquipment.getWardId()!=null){
            equipmentDetail.setFloor(wxDeptMapper.selectByPrimaryKey(wxEquipment.getWardId()).getDeptNumber());
            equipmentDetail.setWard(wxDeptMapper.selectByPrimaryKey(wxEquipment.getWardId()).getDeptName());
        }
        if (wxEquipment.getDepartmentId()!=null){
            equipmentDetail.setDept(wxDeptMapper.selectByPrimaryKey(wxEquipment.getDepartmentId()).getDeptName());
        }
        if (wxEquipment.getBedNumber()!=null){
            WxDept wxDept = wxDeptMapper.selectByPrimaryKey(wxEquipment.getBedNumber());
            if (wxDept != null) {
                equipmentDetail.setBedNumber(wxDept.getDeptName());
            }
        }
        equipmentDetail.setEquipmentStatus(wxEquipment.getEquipmentStatus());
        equipmentDetail.setEquipmentId(wxEquipment.getEquipmentId());

        return equipmentDetail;
    }

    @Override
    public PageInfo getEquipmentRecord(Integer pageNo, Integer pageSize, String equipmentId, Integer recordType) {
        PageHelper.startPage(pageNo,pageSize);
        List<EquipmentRecord> equipmentRecordList = new LinkedList<>();
        WxEquipment wxEquipment = wxEquipmentMapper.selectByPrimaryKey(equipmentId);
        if (recordType == ResultStatus.RECORD_TYPE_CLEAN || recordType == ResultStatus.RECORD_TYPE_MAINTAIN) {
            WxAftersale wxAftersale = new WxAftersale();
            wxAftersale.setEquipmentId(wxEquipment.getEquipmentId());
            List<WxAftersale> wxAftersales = wxAftersaleMapper.selectByExample(wxAftersale);
            for (WxAftersale aftersale : wxAftersales) {
                EquipmentRecord equipmentRecord = new EquipmentRecord();
                equipmentRecord.setUserAccount(aftersale.getUserId());
                equipmentRecord.setOperateFee(aftersale.getPrice());
                equipmentRecord.setStartTime(aftersale.getcTime());
                equipmentRecord.setOperateType(aftersale.getAfterSaleType());
                equipmentRecordList.add(equipmentRecord);
            }
        }
        if (recordType == ResultStatus.ORECORD_TYPE_RENT) {
            WxShareOrders wxShareOrders = new WxShareOrders();
            wxShareOrders.setEquipmentId(wxEquipment.getEquipmentId());
            wxShareOrders.setOrderStatus(ResultStatus.ORDERSTATUS_IN);
            List<WxShareOrders> shareOrders = wxShareOrdersMapper.selectByExample(wxShareOrders);
            for (WxShareOrders shareOrder : shareOrders) {
                EquipmentRecord equipmentRecord = new EquipmentRecord();
                equipmentRecord.setUserAccount(shareOrder.getUserId());
                equipmentRecord.setOperateFee(shareOrder.getPrice());
                equipmentRecord.setStartTime(shareOrder.getStartTime());
                equipmentRecord.setOperateType(ResultStatus.ORECORD_TYPE_RENT);
                equipmentRecordList.add(equipmentRecord);
            }
        }
        return new PageInfo(equipmentRecordList,pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindEquipment(WxEquipment wxEquipment) {
        WxEquipment equipmentInfo = wxEquipmentMapper.selectByEquipmentNumber(wxEquipment.getEquipmentNumber());

        WxEquipment bindEquipment = new WxEquipment();
        bindEquipment.setDepartmentId(wxEquipment.getDepartmentId());
        bindEquipment.setWardId(wxEquipment.getWardId());
        bindEquipment.setBedNumber(wxEquipment.getBedNumber());
        List<WxEquipment> bindEquipmentList = wxEquipmentMapper.selectByExample(bindEquipment);
        for (WxEquipment equipment : bindEquipmentList) {
            if (equipment.getCategoryId().equalsIgnoreCase(equipmentInfo.getCategoryId())){
                return ResultStatus.BINDED;
            }
        }
        wxEquipment.setIsBinding(ResultStatus.ISBINDING);
        wxEquipment.setEquipmentId(equipmentInfo.getEquipmentId());
        return wxEquipmentMapper.updateByPrimaryKeySelective(wxEquipment);

    }

    @Override
    public WxEquipment selectByEquipmentNumber(String equipmentNumber) {

        return wxEquipmentMapper.selectByEquipmentNumber(equipmentNumber);
    }

    @Override
    public int deleteEquipment(String equipmentId) {
        return wxEquipmentMapper.deleteByPrimaryKey(equipmentId);
    }
@Override
    public boolean isEqTimeTrue(String equipmentNumber) {
        Date date = new Date();
        //查询设备对应医院的使用区间
        boolean flag=false;
        List<Map<String, String>> eqTimes = wxEquipmentMapper.getEqTimes(equipmentNumber);
        if(eqTimes!=null){
            for (Map<String, String> eqTime : eqTimes) {
                String startTimeO = eqTime.get("startTimeO");
                String endTimeO = eqTime.get("endTimeO");
                long time = DateUtils.putDate(date);
                long[] longs = DateUtils.calculationOriginValue(time, time);
                flag = DateUtils.isIntersection(longs[0], longs[0], Integer.valueOf(startTimeO).longValue(), Integer.valueOf(endTimeO).longValue());
                if(flag){
                    break;
                }
            }
        }
        return flag;

}

    @Override
    public WxEquipment getByBluetoothName(String bluetoothName) {
        return wxEquipmentMapper.getByBluetoothName(bluetoothName);
    }

}
