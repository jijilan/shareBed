package com.sharebedapp.jijl.mapper;
import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.wrap.HospitalEquipmentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("wxEquipmentMapper")
public interface WxEquipmentMapper extends BaseMapper<WxEquipment,String> {

    WxEquipment getEquipmentByEqNumber(String equipmentNumber);

    List<Map<String,Object>> getEquipmentList(@Param("equipmentNumber")String equipmentNumber,
                                              @Param("categoryId") String categoryId,
                                              @Param("hospitalName") String hospitalName);

    Map<String,Object> getEquipment(String equipmentId);

    List<String> getEquipmentNumberList();

    Integer countByCategoryId(@Param("hospitalId") String hospitalId,
                              @Param("categoryId") String categoryId,
                              @Param("isNull") String isNull,
                              @Param("equipmentStatus") Integer equipmentStatus,
                              @Param("userId") String userId);

    List<WxEquipment> selectByUserId(String userId);

    List<WxEquipment> getEquipmentAvailable(@Param("totalNum") Integer totalNum ,
                                            @Param("hospitalId") String hospitalId);

    List<Map<String,Object>> getShareorderHospital(String equipmentNumber);

    List<WxEquipment> selectByHospitalId(String hospitalId);

    List<WxEquipment> getByCategoryId(String categoryId);

    List<HospitalEquipmentInfo> getEquipmentListByHospitalId(@Param("hospitalId") String hospitalId,
                                                             @Param("categoryId") String categoryId,
                                                             @Param("department") String department,
                                                             @Param("ward") String ward);

    int unbindEquipment(String equipmentNumber);

    Integer getEquipmentAmountByHospital(@Param("categoryId") String categoryId,
                                         @Param("hospitalId") String hospitalId);

    Integer countTotalByCategoryId(@Param("hospitalId") String hospitalId,
                                   @Param("categoryId") String categoryId);

    Integer countAvailableByCategoryId(@Param("hospitalId") String hospitalId,
                                       @Param("categoryId") String categoryId);

    WxEquipment selectByEquipmentNumber(String equipmentNumber);

    List<Map<String,String>> getEqTimes(String equipmentNumber);

    int updateHospitalNull(String equipmentId);

    WxEquipment getByBluetoothName(String bluetoothName);
}