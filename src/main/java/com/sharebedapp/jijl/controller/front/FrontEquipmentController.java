package com.sharebedapp.jijl.controller.front;

import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.wrap.EquipmentDetail;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxAgentService;
import com.sharebedapp.jijl.service.WxEquipmentService;
import com.sharebedapp.jijl.service.WxShareOrdersService;
import com.sharebedapp.jijl.service.WxUserService;
import com.sharebedapp.jijl.utils.EquipmentUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: liujiebang
 * @Date: Create in 2018/8/28
 * @Description:
 **/
@RestController
@Slf4j
public class FrontEquipmentController {


    @Autowired
    private WxShareOrdersService wxShareOrdersService;

    @Autowired
    private WxEquipmentService wxEquipmentService;

    @Autowired
    private WxAgentService wxAgentService;

    @Autowired
    private WxUserService wxUserService;


    /**
     * 获取设备状态
     * @param equipmentNumber 设备序列号
     * @return
     */
    @PostMapping("/getEquipmentStatus")
    public ResultView switchLock(String equipmentNumber) {
        if (StringUtils.isEmpty(equipmentNumber)) {
            return ResultView.error(ResultEnum.CODE_170);
        }
        WxEquipment equipmentByEqNumber = wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
        if(equipmentByEqNumber==null){
            return ResultView.error(ResultEnum.CODE_206);
        }
        return ResultView.ok(equipmentByEqNumber.getEquipmentStatus());

    }
    /**
     * 开锁-关锁
     *
     * @param equipmentNumber 设备序列号
     * @param swift           1.开锁 2.关锁
     * @return
     */
    @RequestMapping("/front/equipment/switchLock")
    public ResultView switchLock(HttpServletRequest request, String equipmentNumber, String swift) {
        if (StringUtils.isEmpty(equipmentNumber)) {
            return ResultView.error(ResultEnum.CODE_170);
        }
        if (swift == null && (swift != "1" || swift != "2")) {
            return ResultView.error(ResultEnum.CODE_203);
        }
        WxEquipment wxEquipment = wxEquipmentService.selectByEquipmentNumber(equipmentNumber);
        if (wxEquipment != null){
            if (wxEquipment.getEquipmenLockType() == 1){
                return ResultView.error(ResultEnum.CODE_191);
            }
        }
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        return wxShareOrdersService.switchLock(userId, equipmentNumber, swift);

    }

    /**
     * 设备心跳地址
     *
     * @param macno        设备号
     * @param status       1表示锁开，0表示锁关
     * @param order_id     支付流水号id
     * @param cell_voltage 电池电压
     * @param ctime        返回时间戳
     * @return
     */
    @RequestMapping("/equipmentReturn")
    public ResultView equipmentReturn(String macno, String status, String order_id, String cell_voltage, Long ctime) {
        log.info("设备序列号：{}", macno);
        log.info("设备当前状态:【1表示锁开，0表示锁关】{}", status);
        log.info("使用当前设备订单号：{}", order_id);
        Map<String, String> shareOrdersEqNumber = wxShareOrdersService.getShareOrdersEqNumber(macno);
        if ("0".equals(status)) {
            //维修人员或保洁人员
            if (shareOrdersEqNumber == null) {
                //没有 订单的支付流水号 则为保洁或维修人员
                WxEquipment equipment = wxEquipmentService.getEquipmentByEqNumber(macno);
                if (equipment.getEquipmentStatus() != ResultStatus.EQUIOMENT_STATUS_1) {
                    equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_1);
                    wxEquipmentService.upEquipment(equipment);
                }
                return ResultView.ok();

            } else {
                String outTradeNo = shareOrdersEqNumber.get("outTradeNo");
                //计算电池电量
                // double cellVoltage = calculatedElectricQuantity(ResultStatus.KWH_MAX, ResultStatus.KWH_MIN, Double.valueOf(cell_voltage));
                wxShareOrdersService.isOutTime(macno, ctime * 1000, outTradeNo, cell_voltage);
            }
        }
        return ResultView.ok();
    }

    /**
     * 硬件回调地址设置--equipmentNumber 序列号集合 多个以逗号分割
     *
     * @param equipmentNumbers
     * @return
     */
    @GetMapping("/EquipmentReturenSetUp")
    public ResultView EquipmentReturenSetUp(String equipmentNumbers) {
        return EquipmentUtil.setEquipmentReturnUrl(equipmentNumbers);
    }

    @GetMapping("/front/getUserEquipmentList")
    public ResultView getUserEquipmentList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           HttpServletRequest request ,String hospitalId){

        HashMap<String, Object> map = new HashMap<>();
        String consumerId = (String) request.getAttribute(ResultStatus.USER_ID);
        log.info("登录的用户是：" + consumerId);
        if (consumerId != null) {
            map = wxUserService.getUserEquipmentList(pageNo, pageSize, hospitalId, consumerId);
        }else {
            consumerId = (String) request.getAttribute(ResultStatus.AGENT_ID);
            if (consumerId != null) {
                map = wxAgentService.getAgentEquipmentList(pageNo, pageSize, hospitalId, consumerId);
            }
        }
        return ResultView.ok(map);

    }

    @GetMapping("/front/getUserEquipmentInfo")
    public ResultView getUserEquipmentInfo(String equipmentId){
        EquipmentDetail equipmentDetail =  wxEquipmentService.getEquipmentInfo(equipmentId);
        return ResultView.ok(equipmentDetail);
    }

    @GetMapping("/front/getUserEquipmentRecord")
    public ResultView getUserEquipmentRecord(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                             String equipmentId, Integer recordType){

        PageInfo pageInfo = wxEquipmentService.getEquipmentRecord(pageNo, pageSize, equipmentId,recordType);

        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo ,"equipmentRecordList"));
    }
}
