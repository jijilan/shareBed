package com.sharebedapp.jijl.controller.front;

import com.github.pagehelper.util.StringUtil;
import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxFinance;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.model.sub.WxShareOrdersSub;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/front")
public class FrontShareOrdersController {

    @Autowired
    private WxShareOrdersService wxShareOrdersService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxEquipmentService wxEquipmentService;
    @Autowired
    private WxHospitalService wxHospitalService;
    @Autowired
    private WxRechargeService wxRechargeService;
    @Autowired
    private WxFinanceService wxFinanceService;

    /***
     * 创建预约订单
     * @param equipmentNumber 设备唯一编码
     * @return
     */
    @PostMapping("/creatShareOrders")
    public ResultView creatShareOrders(HttpServletRequest request, String equipmentNumber) {
        if (StringUtils.isEmpty(equipmentNumber)) {
            return ResultView.error(ResultEnum.CODE_170);
        }
       String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxUser user = wxUserService.getByUserId(userId);
        if (user == null) {
            return ResultView.error(ResultEnum.CODE_163);
        }

        //查询是否有未支付，或进行中的订单 有就不能创建订单
        if (wxShareOrdersService.getByUserId(user.getUserId()).size() > 0) {
            return ResultView.error(ResultEnum.CODE_211);
        }

        if (StringUtil.isEmpty(equipmentNumber)) {
            return ResultView.error(ResultEnum.CODE_203);
        }
        //设备
        WxEquipment equipment = wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
        if (equipment == null) {
            return ResultView.error(ResultEnum.CODE_206);
        }
        if (equipment.getEquipmentStatus() == 2) {
            return ResultView.error(ResultEnum.CODE_167);
        }
        //用户就创建订单
        if (user.getUserType() == ResultStatus.WXUSER_USERTYPE_1) {
            if (equipment.getEquipmentStatus() == 3) {
                return ResultView.error(ResultEnum.CODE_221);
            }
            //查询押金
            WxFinance finance = wxFinanceService.isBail(userId);
            if (finance == null) {
                return ResultView.error(ResultEnum.CODE_224);
            } else {
                if (finance.getFinanceType() != ResultStatus.WXFINACE_FINANCEETYPE_5) {
                    return ResultView.error(ResultEnum.CODE_224);
                }
            }

            if (equipment.getEquipmentStatus() == 1) {
                //创建订单
                return wxShareOrdersService.creatShareOrders(userId, equipment, equipmentNumber);
            }
        }
        //保洁
        if (user.getUserType() == ResultStatus.WXUSER_USERTYPE_3) {
            if (equipment.getEquipmentStatus() == ResultStatus.EQUIOMENT_STATUS_FAILURE) {
                return ResultView.error(ResultEnum.CODE_221);
            }
            Map<String, Object> map = new HashMap<>();
            //开锁成功
            equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
            wxEquipmentService.upEquipment(equipment);
            map.put("wxShareOrdersSub", wxEquipmentService.getShareorderHospital(equipmentNumber));
            map.put("msg", "保洁人员清理中");
            return ResultView.ok(map);

        }
        //维修
        if (user.getUserType() == ResultStatus.WXUSER_USERTYPE_2) {
            Map<String, Object> map = new HashMap<>();
            //开锁成功
            equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
            wxEquipmentService.upEquipment(equipment);
            map.put("wxShareOrdersSub", wxEquipmentService.getShareorderHospital(equipmentNumber));
            map.put("msg", "维修人员维修中");
            return ResultView.ok(map);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }


    /***
     * 结束预约订单
     * @param macno        设备号
     * @param cell_voltage 电池电量0-100
     * @return
     */
    @PostMapping("/outShareOrders")
    public ResultView outShareOrders(String macno, String cell_voltage) {
        if (StringUtil.isEmpty(macno)) {
            return ResultView.error(ResultEnum.CODE_203);
        }
        Map<String, String> shareOrdersEqNumber = wxShareOrdersService.getShareOrdersEqNumber(macno);
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
            return  wxShareOrdersService.isOutTime(macno, System.currentTimeMillis(), outTradeNo, cell_voltage);
        }
    }


    /***
     * 使用记录
     * @param pageNo 页数
     * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getUserShareOrdersList")
    public ResultView getUserGoodsOrdersList(HttpServletRequest request, @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);

        return wxShareOrdersService.getUserShareOrdersList(userId, pageNo, pageSize);
    }

    /***
     * 使用记录总时间
     * @return
     */
    @GetMapping("/getUserTime")
    public ResultView getUserTime(HttpServletRequest request) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        return wxShareOrdersService.getUserTime(userId);
    }


    /***
     * 查询账户是否有未支付或进行中的订单
     * @param request
     * @return
     */
    @GetMapping(value = "/isOutTimeOrder")
    public ResultView isOutTimeOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute(ResultStatus.USER_ID);
        WxShareOrdersSub wxShareOrdersSub = wxShareOrdersService.getLastOrders(userId);
        if (wxShareOrdersSub == null) {
            return ResultView.ok();
        } else {
            if (wxShareOrdersSub.getOrderStatus() == ResultStatus.ORDERSTATUS_PAY || wxShareOrdersSub.getOrderStatus() == ResultStatus.ORDERSTATUS_IN) {
                Map<String, Object> map = new HashMap<>();
                map.put("wxShareOrdersSub", wxShareOrdersSub);
                return ResultView.ok(map);
            } else {
                return ResultView.ok();
            }
        }
    }
}
