package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.wrap.EquipmentDetail;
import com.sharebedapp.jijl.model.wrap.HospitalEquipmentInfo;
import com.sharebedapp.jijl.result.ResultView;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author hzm
 * 设备业务接口
 */
public interface WxEquipmentService {

    /**
     * 条件查询设备列表
     * @param equipmentNumber 设备唯一编号
     * @param categoryId 设备分类id
     * @param hospitalName 设备所属医院名称
     * @param pageNo 分页展示页码
     * @param pageSize 分页展示页大小
     * @return 分页信息
     */
    ResultView getEquipmentList(String equipmentNumber, String categoryId, String hospitalName, Integer pageNo, Integer pageSize);

    int addEquipment(WxEquipment wxEquipment);

    WxEquipment getEquipmentByEqNumber(String equipmentNumber);

    WxEquipment getEquipmentByEquipmentId(String equipmentId);

    int upEquipment(WxEquipment wxEquipment);

    ResultView getEquipment(String equipmentId);

    List<String> getEquipmentNumberList();

    List<Map<String,Object>> getShareorderHospital(String equipmentNumber);

    PageInfo<HospitalEquipmentInfo> getEquipmentListByHospitalId(Integer pageNo, Integer pageSize, String hospiatlId, String categoryId, String department, String ward);

    boolean getByCategoryId(String categoryId);

    int unbindEquipment(String equipmentNumber);

    EquipmentDetail getEquipmentInfo(String equipmentId);

    PageInfo getEquipmentRecord(Integer pageNo, Integer pageSize, String equipmentId, Integer recordType);

    int bindEquipment(WxEquipment wxEquipment);

    WxEquipment selectByEquipmentNumber(String equipmentNumber);

    int deleteEquipment(String equipmentId);

	boolean isEqTimeTrue(String equipmentNumber);

    WxEquipment getByBluetoothName(String bluetoothName);
}
