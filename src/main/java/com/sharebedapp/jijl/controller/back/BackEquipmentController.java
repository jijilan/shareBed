package com.sharebedapp.jijl.controller.back;


import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxEquipmentService;
import com.sharebedapp.jijl.utils.EquipmentUtil;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author hzm
 */
@RestController
@RequestMapping(value = "/back")
public class BackEquipmentController {

    private final WxEquipmentService wxEquipmentService;

    @Autowired
    public BackEquipmentController(WxEquipmentService wxEquipmentService) {
        this.wxEquipmentService = wxEquipmentService;
    }

    /***
     * 设备详情
     *  @param equipmentId 设备id
     * @return
     */
    @GetMapping(value = "/getEquipment")
    public ResultView getEquipment(String equipmentId){
        if(StringUtil.isEmpty(equipmentId)){
            return  ResultView.error(ResultEnum.CODE_203);
        }
        return wxEquipmentService.getEquipment(equipmentId);
    }
    /***
     * 添加设备
     *  @param equipmentNumber 设备编号
     * @param categoryId 设备类型
     * @param equipmenLockType 设备锁类型 1蓝牙 2机械
     * @param bluetoothName 蓝牙名称
     * @return
     */
    @PostMapping(value = "/addEquipment")
    public ResultView addEquipment(String equipmentNumber, String categoryId,Integer equipmenLockType,String bluetoothName){
        if(StringUtil.isEmpty(equipmentNumber)){
            return ResultView.error(ResultEnum.CODE_203);
        }
        if(categoryId==null){
            return ResultView.error(ResultEnum.CODE_158);
        }
        if(wxEquipmentService.getEquipmentByEqNumber(equipmentNumber)!=null){
            return ResultView.error(ResultEnum.CODE_147);
        }
        if(equipmenLockType==null || equipmenLockType!=1&&equipmenLockType!=2){
            return ResultView.error(ResultEnum.CODE_185);
        }
        WxEquipment wxEquipment=new WxEquipment();
        wxEquipment.setEquipmentId(IdentityUtil.identityId("EQ"));
        wxEquipment.setEquipmentNumber(equipmentNumber);
        wxEquipment.setEquipmenLockType(equipmenLockType);
        if(equipmenLockType==1){
            if(StringUtil.isEmpty(bluetoothName)){
                return ResultView.error(ResultEnum.CODE_186);
            }
            if(wxEquipmentService.getByBluetoothName(bluetoothName) !=null){
                return ResultView.error(ResultEnum.CODE_194);
            }
            wxEquipment.setBluetoothName(bluetoothName);
        }
        wxEquipment.setCategoryId(categoryId);
        wxEquipment.setcTime(new Date());
        if(wxEquipmentService.addEquipment(wxEquipment)>0){
            EquipmentUtil.setEquipmentReturnUrl(equipmentNumber);
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    /***
     * 设备列表
     *  @param equipmentNumber 设备编号
     * @param categoryId 设备类型
     * @param hospitalName 医院名称
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping(value = "/getEquipmentList")
    public ResultView getEquipmentList(String equipmentNumber, String categoryId,String hospitalName ,@RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){

        return wxEquipmentService.getEquipmentList(equipmentNumber,categoryId,hospitalName,pageNo,pageSize);
    }


    /***
     * 设备报损
     *  @param equipmentId 设备主键id
       *  @param equipmentStatus 设备状态 1：空闲 2：使用中 3.故障
     * @return
     */
    @PostMapping(value = "/upEquipmentStatus")
    public ResultView upEquipmentStatus(String equipmentId,Integer equipmentStatus){
       if(StringUtil.isEmpty(equipmentId)){
           return  ResultView.error(ResultEnum.CODE_203);
       }
       if(equipmentStatus==null){
           return  ResultView.error(ResultEnum.CODE_161);
       }
        if(!equipmentStatus.equals(1)&&!equipmentStatus.equals(3)){
            return  ResultView.error(ResultEnum.CODE_159);
        }
       WxEquipment wxEquipment =wxEquipmentService.getEquipmentByEquipmentId(equipmentId);
        if(wxEquipment!=null){
                    wxEquipment.setEquipmentStatus(equipmentStatus);
                } else{
            return ResultView.error(ResultEnum.CODE_1002);
        }
        return wxEquipmentService.upEquipment(wxEquipment)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);
    }

    /***
     * 解绑设备
     *  @param equipmentId 设备主键id
     * @return
     */
    @PostMapping(value = "/upEquipmentIsBinding")
    public ResultView upEquipmentIsBinding(String equipmentId) {
        if(StringUtil.isEmpty(equipmentId)){
            return  ResultView.error(ResultEnum.CODE_203);
        }
        WxEquipment wxEquipment =wxEquipmentService.getEquipmentByEquipmentId(equipmentId);
        if(wxEquipment!=null) {
            wxEquipment.setIsBinding(1);
        }else{
            return ResultView.error(ResultEnum.CODE_1002);
        }
        return wxEquipmentService.upEquipment(wxEquipment)>0?ResultView.ok():ResultView.error(ResultEnum.CODE_2);
    }

    @GetMapping("/getEquipmentNumberList")
    public ResultView getEquipmentNumberList(){
        List<String> equipmentNumberList = wxEquipmentService.getEquipmentNumberList();
        return ResultView.ok(equipmentNumberList);
    }

    @DeleteMapping("/deleteEquipment")
    public ResultView deleteEquipment(String equipmentId){
        int flag = wxEquipmentService.deleteEquipment(equipmentId);
        if (flag > 0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
}
