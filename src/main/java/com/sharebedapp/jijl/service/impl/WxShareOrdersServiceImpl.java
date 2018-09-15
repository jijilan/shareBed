package com.sharebedapp.jijl.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.*;
import com.sharebedapp.jijl.model.sub.WxShareOrdersInfo;
import com.sharebedapp.jijl.model.sub.WxShareOrdersSub;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.*;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.EquipmentUtil;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WxShareOrdersServiceImpl implements WxShareOrdersService {
    @Autowired
    private WxShareOrdersMapper wxShareOrdersMapper;
    @Autowired
    private WxRechargeService wxRechargeService;
    @Autowired
    private WxHospitalService wxHospitalService;
    @Autowired
    private WxFinanceService wxFinanceService;
    @Autowired
    private WxFinanceMapper wxFinanceMapper;
    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;
    @Autowired
    private WxAgentService wxAgentService;
    @Autowired
    private WxRechargeMapper wxRechargeMapper;
    @Autowired
    private WxEquipmentService  wxEquipmentService;
    @Autowired
    private WxUserMapper wxUserMapper;



    @Override
    public ResultView getShareOrderseList(String shareOrderId, String userPhone, String categoryId,String hospitalName, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        PageInfo pageInfo = new PageInfo(wxShareOrdersMapper.getShareOrderseList(shareOrderId, userPhone, categoryId,hospitalName), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo, "shareOrdersList"));
    }

    @Override
    public ResultView getShareOrderse(String shareOrderId) {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("shareOrders", wxShareOrdersMapper.getShareOrderse(shareOrderId));
        return ResultView.ok(map);
    }

    @Override
    public ResultView getUserShareOrdersList(String userId, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<WxShareOrdersInfo> list=wxShareOrdersMapper.getUserShareOrdersTimeList(userId);
        for (WxShareOrdersInfo wxShareOrdersInfo : list) {
            String time = wxShareOrdersInfo.getcTime();
            List<WxShareOrdersInfo> listOrder = wxShareOrdersMapper.getUserShareOrdersList(userId,time);
            wxShareOrdersInfo.setListUserShareOrders(listOrder);
        }
        PageInfo pageInfo = new PageInfo(list, pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo, "userShareOrdersList"));
    }

    @Override
    public ResultView getUserTime(String userId) {
        HashMap<String,String> map=new HashMap<>(1);
        map.put("sumTime","0");
        Map<String, Object> userTime = wxShareOrdersMapper.getUserTime(userId);
        if(userTime!=null){
            BigDecimal sumTime = (BigDecimal) userTime.get("sumTime");
            String s1 = DateUtils.formatDateTime2(sumTime.longValue());
            map.put("sumTime",s1);
        }
        return ResultView.ok(map);
    }

    @Override
    public int getShareOrderCount(String categoryId) {
        return wxShareOrdersMapper.getShareOrderCount(categoryId);
    }

    @Override
    public int getTodayAccount(String categoryId) {
        return wxShareOrdersMapper.getTodayAccount(categoryId);
    }

    @Override
    public int getMonthAccount(String categoryId) {
        return wxShareOrdersMapper.getMonthAccount(categoryId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView isOutTime(String equipmentNumber, Long ctime, String outTradeNo,String cell_voltage) {

      WxShareOrders wxShareOrders = wxShareOrdersMapper.getByoutTradeNo(outTradeNo);

        if(wxShareOrders.getIsFlag()==ResultStatus.ORDER_ISFLAG_YES&&wxShareOrders.getOrderStatus()==ResultStatus.ORDERSTATUS_IN){
            //下订单时间
            Date startTime = wxShareOrders.getLeaseStartTime();
            long sumMin = 0l;
            try {
                //计算总使用时间
                sumMin = DateUtils.checkDate(DateUtils.DatetoString(startTime), DateUtils.DatetoString(new Date(ctime)));
            } catch (Exception e) {
                e.printStackTrace();
                log.info("时间转换异常:{}", e);
            }
            //获取收费规则信息
            String rechargeId = wxShareOrders.getRechargeId();
            WxRecharge wxRecharge = wxRechargeService.getByRechargeId(rechargeId);
            Integer rechargeType = wxRecharge.getRechargeType();
            //至少使用 小时
            Integer minHour = wxRecharge.getMinHour();
            //转成秒
            long min = minHour.longValue()*3600;
            //收费金额
            BigDecimal rechargePrice = wxRecharge.getRechargePrice();
            //超时费用
            BigDecimal overtimePrice = wxRecharge.getOvertimePrice();

            //费用
            BigDecimal price=null;
            double time;
            double money;
            //可用时长
             long  availableTimeLength= wxShareOrders.getAvailableTimeLength();
            //没有超时
            if (sumMin <= availableTimeLength) {
                //次数收费
                if (rechargeType == ResultStatus.RECHARGE_TYPE_N) {
                    price=rechargePrice;
                }
                //时间收费
                if (rechargeType == ResultStatus.RECHARGE_TYPE_H) {
                    if(sumMin>min){   //使用时间 大于 至少使用时间  直接算
                        time=((double) sumMin)/3600;
                        money  = Math.round(time) *  rechargePrice.doubleValue();
                        price=BigDecimal.valueOf(money);
                    }else{    //小于使用的时间

                        // 用至少使用的时间来算
                        time=((double) min)/3600;
                        money  =Math.round(time) *  rechargePrice.doubleValue();
                        price=BigDecimal.valueOf(money);
                    }
                }
            }else{   //超时
                //   超时的收费标准一致
                //超时时间=总使用时间-可使用时间
                long cmin=sumMin-availableTimeLength;
                //次数收费
                if (rechargeType == ResultStatus.RECHARGE_TYPE_N) {
                    //次数费用=次数的收费 + 超时的收费
                    money=rechargePrice.doubleValue()+Math.round(((double) cmin)/3600)*overtimePrice.doubleValue();
                    price=BigDecimal.valueOf(money);
                }
                //时间收费
                if (rechargeType == ResultStatus.RECHARGE_TYPE_H) {
                    //时间费用=可使用的金额+超时的金额
                    money=rechargePrice.doubleValue()*Math.round(((double) availableTimeLength)/3600)+Math.round(((double) cmin)/3600)*overtimePrice.doubleValue();
                    price=BigDecimal.valueOf(money);
                }
            }
            wxShareOrders.setLeaseEndTime(new Date(ctime));
            wxShareOrders.setPrice(price);
            wxShareOrders.setOrderStatus(ResultStatus.ORDERSTATUS_PAY);
            wxShareOrders.setIsFlag(ResultStatus.ORDER_ISFLAG_YES);

            wxShareOrdersMapper.updateByPrimaryKeySelective(wxShareOrders);
            WxEquipment equipment = wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
            equipment.setCellVoltage(cell_voltage);
            equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_1);
            wxEquipmentMapper.updateByPrimaryKeySelective(equipment);
            Map<String,Object> map=new HashMap<>(1);
            map.put("price",price);
            return  ResultView.ok(map);
        }
        return  ResultView.error(ResultEnum.CODE_2);
    }

    @Override
    public WxShareOrdersSub getLastOrders(String userId) {
        return wxShareOrdersMapper.getLastOrders(userId);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payCallback(String outTradeNo, Integer payType) {
        WxShareOrders shareOrders = wxShareOrdersMapper.getByoutTradeNo(outTradeNo);
        if(ResultStatus.ORDERSTATUS_Y!=shareOrders.getOrderStatus()){
            shareOrders.setPayTime(new Date());
            shareOrders.setPayType(payType);
            shareOrders.setOrderStatus(ResultStatus.ORDERSTATUS_Y);
            wxShareOrdersMapper.updateByPrimaryKeySelective(shareOrders);
            //总费用
            Double sumPrice = shareOrders.getPrice().doubleValue();
            String userId = shareOrders.getUserId();
            String hospitalId = shareOrders.getHospitalId();
            Date cTime = new Date();

            //  用户消费财务
            WxFinance uFinanc=new WxFinance();
            uFinanc.setFinanceId(IdentityUtil.identityId("FI"));
            uFinanc.setUserId(userId);
            uFinanc.setHospitalId(hospitalId);
            uFinanc.setOutTradeNo(shareOrders.getOutTradeNo());
            uFinanc.setExpensesAmount(shareOrders.getPrice());
            uFinanc.setFinanceType(ResultStatus.WXFINACE_FINANCEETYPE_2);
            uFinanc.setPayType(payType);
            uFinanc.setIsFlag(ResultStatus.ISFLAG_Y);
            uFinanc.setcTime(cTime);
            wxFinanceMapper.insertSelective(uFinanc);

            //医院数据
            WxHospital hospital = wxHospitalService.getByHospitalId(shareOrders.getHospitalId());
            //设备数据
            WxEquipment equipment = wxEquipmentMapper.selectByPrimaryKey(shareOrders.getEquipmentId());
            //设备认购商 利益
            Double uMoney=0.00;
            //渠道商利益
            Double hMoney=0.00;
            //有认购商购买了
                if(equipment.getUserId()!=null){
                //认购商比例
                Integer purchaserCommissionRate = hospital.getPurchaserCommissionRate();
                //认购商 财务
                WxFinance rFinanc=new WxFinance();
                // 查询用户的收益
                WxFinance mFinanc = wxFinanceMapper.balance(equipment.getUserId(),ResultStatus.PAYTYPE_BALANCE);
                Double mbalance =0.00;
                if(mFinanc!=null){
                    mbalance=mFinanc.getBalance().doubleValue();
                }
                uMoney=(purchaserCommissionRate*sumPrice)/100;
                rFinanc.setFinanceId(IdentityUtil.identityId("FI"));
                rFinanc.setUserId(equipment.getUserId());
                rFinanc.setHospitalId(shareOrders.getHospitalId());
                rFinanc.setOutTradeNo(shareOrders.getOutTradeNo());
                rFinanc.setBalance(BigDecimal.valueOf(mbalance+uMoney));
                rFinanc.setRevenueAmount(BigDecimal.valueOf(uMoney));
                rFinanc.setFinanceType(ResultStatus.WXFINACE_FINANCEETYPE_4);
                rFinanc.setBalanceType(ResultStatus.FINANCE_BALANCETYPE_1);
                rFinanc.setIsFlag(ResultStatus.ISFLAG_Y);
                rFinanc.setcTime(cTime);
                wxFinanceMapper.insertSelective(rFinanc);
            }

            //医院比例
            Integer hospitalCommissionRate = hospital.getHospitalCommissionRate();
            //医院总收益
            hMoney=(hospitalCommissionRate*sumPrice)/100;
            List<WxAgent> list=wxAgentService.getEarningsAgetnList(hospital.getHospitalId());
            if(list.size()>0){
            for (WxAgent wxAgent : list) {
                //渠道商 财务
                //分成比例
                //正常的渠道商
             if(wxAgent.getIsFlag()==1){
                Integer proportion = wxAgent.getProportion();
                Double  huMoney =(hMoney*proportion)/100;
                //查询渠道商的最新余额
                WxFinance mFinance=   wxFinanceService.getByAgentId(wxAgent.getAgentId());
                Double mbalance =0.00;
                if(mFinance!=null){
                    mbalance=mFinance.getBalance().doubleValue();
                }
                WxFinance qFinanc=new WxFinance();
                qFinanc.setFinanceId(IdentityUtil.identityId("FI"));
                qFinanc.setAgentId(wxAgent.getAgentId());
                qFinanc.setHospitalId(hospitalId);
                qFinanc.setOutTradeNo(shareOrders.getOutTradeNo());
                qFinanc.setBalance(BigDecimal.valueOf(mbalance+huMoney));
                qFinanc.setRevenueAmount(BigDecimal.valueOf(huMoney));
                qFinanc.setFinanceType(ResultStatus.WXFINACE_FINANCEETYPE_4);
                qFinanc.setBalanceType(ResultStatus.FINANCE_BALANCETYPE_1);
                qFinanc.setIsFlag(ResultStatus.ISFLAG_Y);
                qFinanc.setcTime(cTime);
                wxFinanceMapper.insertSelective(qFinanc);
            }
            }
            }
        }
    }

    @Override
    public List<WxShareOrders> getByUserId(String userId) {
        return wxShareOrdersMapper.getByUserId(userId);
    }

    @Override
    public Map<String,String> getShareOrdersEqNumber(String equipmentNumber) {
        return wxShareOrdersMapper.getShareOrdersEqNumber(equipmentNumber);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView switchLock(String userId, String equipmentNumber, String swift) {
        //开锁
        if("1".equals(swift)){
            WxUser user = wxUserMapper.selectByPrimaryKey(userId);
            if(user==null){
                return ResultView.error(ResultEnum.CODE_163);
            }

            //查询是否有未支付 有就不能创建订单
            if(wxShareOrdersMapper.getByUserIdWei(user.getUserId()).size()>0){
                return ResultView.error(ResultEnum.CODE_211);
            } //进行中的订单 有就不能创建订单
            if(wxShareOrdersMapper.getByUserIdJin(user.getUserId()).size()>0){
                return ResultView.error(ResultEnum.CODE_183);
            }
            if(StringUtil.isEmpty(equipmentNumber)){
                return ResultView.error(ResultEnum.CODE_203);
            }
            //设备
            WxEquipment equipment = wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
            if(equipment==null){
                return ResultView.error(ResultEnum.CODE_206);
            }
            if(equipment.getEquipmentStatus()==2){
                return ResultView.error(ResultEnum.CODE_167);
            }
            //用户就创建订单
            if(user.getUserType()==ResultStatus.WXUSER_USERTYPE_1){
                if(equipment.getEquipmentStatus()==3){
                    return ResultView.error(ResultEnum.CODE_221);
                }
                //查询押金
                WxFinance finance = wxFinanceService.isBail(userId);
                if(finance == null){
                    return ResultView.error(ResultEnum.CODE_224);
                }else{
                    if(finance.getFinanceType() != ResultStatus.WXFINACE_FINANCEETYPE_5){
                        return ResultView.error(ResultEnum.CODE_224);
                    }
                }
                if(equipment.getEquipmentStatus()==1){
                    //收费规则
                    List<WxRecharge> byRecharge = wxRechargeService.getByRecharge(equipment.getHospitalId());
                    if(byRecharge.size()==0){
                        return  ResultView.error(ResultEnum.CODE_168);
                    }
                    boolean flag=false;
                    Date startTime =null;
                    Date endTime=null;
                    Date date=null;
                    Long endTimeOriginValue=0L;
                    String rechargeId=null;
                    long nowTime=0L;
                    if(byRecharge.size()>0) {
                        for (WxRecharge recharge : byRecharge) {
                            date=new Date();
                            startTime = recharge.getStartTime();
                            endTime = recharge.getEndTime();
                            rechargeId = recharge.getRechargeId();
                            try {
                                nowTime = DateUtils.replaceYMD(DateUtils.DatetoString(date),DateUtils.DatetoString(startTime));
                                long[] longs = DateUtils.calculationOriginValue(nowTime, nowTime);
                                nowTime=longs[0];
                              //  flag = DateUtils.isInDates(DateUtils.DatetoString(date),DateUtils.DatetoString(startTime),DateUtils.DatetoString(endTime));
                                //是否在区间内
                                endTimeOriginValue=recharge.getEndTimeOriginValue();
                                 flag = DateUtils.isIntersection(nowTime, nowTime, recharge.getStartTimeOriginValue(), recharge.getEndTimeOriginValue());
                            }catch (Exception e){

                                log.info("时间转换异常:{}",e);
                            }
                            //判断时间是否在 区间内
                            if(flag){
                                flag=true;
                                break;
                            }
                        }
                    }
                    if(flag==false){
                        //不在区间内
                        return  ResultView.error(ResultEnum.CODE_169);
                    }
                    //在

                    //创建订单
                    WxShareOrders wxShareOrders=new WxShareOrders();
                    wxShareOrders.setShareOrderId(IdentityUtil.identityId("ORD"));
                    wxShareOrders.setEquipmentId(equipment.getEquipmentId());
                    wxShareOrders.setHospitalId(equipment.getHospitalId());
                    wxShareOrders.setLeaseStartTime(date);
                    //可用时长
                    long availableTimeLength=0L;
                    try {
                       // availableTimeLength = DateUtils.checkDate(DateUtils.DatetoString(new Date(nowTime)), DateUtils.DatetoString(endTime));
                        availableTimeLength=(endTimeOriginValue.longValue()-nowTime)/1000;
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info("时间转换异常:{}",e);
                    }
                    wxShareOrders.setAvailableTimeLength(availableTimeLength);
                    wxShareOrders.setRechargeId(rechargeId);
                    wxShareOrders.setOrderStatus(ResultStatus.ORDERSTATUS_IN);
                    wxShareOrders.setUserId(userId);
                    wxShareOrders.setStartTime(startTime);
                    wxShareOrders.setEndTime(endTime);
                    wxShareOrders.setIsFlag(ResultStatus.ORDER_ISFLAG_YES);
                    wxShareOrders.setcTime(date);
                    wxShareOrders.setOutTradeNo(IdentityUtil.identityId("SHA"));
                    boolean flags =EquipmentUtil.switchLock(equipmentNumber,swift,wxShareOrders.getOutTradeNo());
                    if (flags){
                        //开锁成功
                        equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
                        wxEquipmentService.upEquipment(equipment);
                        int count = wxShareOrdersMapper.insertSelective(wxShareOrders);
                        Map<String, Object> map = new HashMap<>();
                        map.put("wxShareOrdersSub", wxShareOrdersMapper.getFrontShareOrderse(wxShareOrders.getShareOrderId()));
                        if (count > 0) {
                            return ResultView.ok(map);
                        } else {
                            return ResultView.error(ResultEnum.CODE_2);
                        }
                    }
                }
            }
            //保洁
            if(user.getUserType()==ResultStatus.WXUSER_USERTYPE_3){
                if(equipment.getEquipmentStatus()==ResultStatus.EQUIOMENT_STATUS_FAILURE){
                    return ResultView.error(ResultEnum.CODE_221);
                }
                boolean flags =EquipmentUtil.switchLock(equipmentNumber,swift,userId);
                if (flags==true){
                    Map<String,Object> map = new HashMap<>();
                    //开锁成功
                    equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
                    wxEquipmentService.upEquipment(equipment);
                    map.put("wxShareOrdersSub",wxEquipmentService.getShareorderHospital(equipmentNumber));
                    map.put("msg","保洁人员清理中");
                    return ResultView.ok(map);
                }
            }
            //维修
            if(user.getUserType()==ResultStatus.WXUSER_USERTYPE_2){
                boolean flags =EquipmentUtil.switchLock(equipmentNumber,swift,userId);
                if (flags==true){
                    Map<String,Object> map = new HashMap<>();
                    //开锁成功
                    equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
                    wxEquipmentService.upEquipment(equipment);
                    map.put("wxShareOrdersSub",wxEquipmentService.getShareorderHospital(equipmentNumber));
                    map.put("msg","维修人员维修中");
                    return ResultView.ok(map);
                }
            }

        }
        //关锁
        if("2".equals(swift)){
            WxEquipment equipmentByEqNumber = wxEquipmentService.getEquipmentByEqNumber(equipmentNumber);
            if(equipmentByEqNumber.getEquipmentStatus()!=ResultStatus.EQUIOMENT_STATUS_1){
                return ResultView.error(ResultEnum.CODE_1006);
            }
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_1005);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView creatShareOrders(String userId,WxEquipment equipment, String equipmentNumber) {
        List<WxRecharge> byRecharge = wxRechargeService.getByRecharge(equipment.getHospitalId());
        if(byRecharge.size()==0){
            return  ResultView.error(ResultEnum.CODE_168);
        }
        boolean flag=false;
        Date startTime =null;
        Date endTime=null;
        Date date=null;
        Long endTimeOriginValue=0L;
        String rechargeId=null;
        long nowTime=0L;
        if(byRecharge.size()>0) {
            for (WxRecharge recharge : byRecharge) {
                date=new Date();
                startTime = recharge.getStartTime();
                endTime = recharge.getEndTime();
                rechargeId = recharge.getRechargeId();
                endTimeOriginValue = recharge.getEndTimeOriginValue();
                try {
                    nowTime = DateUtils.replaceYMD(DateUtils.DatetoString(date),DateUtils.DatetoString(startTime));
                    long[] longs = DateUtils.calculationOriginValue(nowTime, nowTime);
                    nowTime=longs[0];
                    //是否在区间内
                    flag = DateUtils.isIntersection(nowTime, nowTime, recharge.getStartTimeOriginValue(), recharge.getEndTimeOriginValue());
                }catch (Exception e){
                    log.info("时间转换异常:{}",e);
                }
                //判断时间是否在 区间内
                if(flag){
                    flag=true;
                    break;
                }
            }
        }
        if(flag==false){
            //不在区间内
            return  ResultView.error(ResultEnum.CODE_169);
        }
        //在
        //创建订单
        WxShareOrders wxShareOrders=new WxShareOrders();
        wxShareOrders.setShareOrderId(IdentityUtil.identityId("ORD"));
        wxShareOrders.setEquipmentId(equipment.getEquipmentId());
        wxShareOrders.setHospitalId(equipment.getHospitalId());
        wxShareOrders.setLeaseStartTime(date);
        //可用时长
        long availableTimeLength=0L;
        try {
         //   availableTimeLength = DateUtils.checkDate(DateUtils.DatetoString(new Date(nowTime)), DateUtils.DatetoString(endTime));
            availableTimeLength=(endTimeOriginValue.longValue()-nowTime)/1000;
        }catch (Exception e){
            e.printStackTrace();
            log.info("时间转换异常:{}",e);
        }
        wxShareOrders.setAvailableTimeLength(availableTimeLength);
        wxShareOrders.setRechargeId(rechargeId);
        wxShareOrders.setOrderStatus(ResultStatus.ORDERSTATUS_IN);
        wxShareOrders.setUserId(userId);
        wxShareOrders.setStartTime(startTime);
        wxShareOrders.setEndTime(endTime);
        wxShareOrders.setIsFlag(ResultStatus.ORDER_ISFLAG_YES);
        wxShareOrders.setcTime(date);
        wxShareOrders.setOutTradeNo(IdentityUtil.identityId("SHA"));
        equipment.setEquipmentStatus(ResultStatus.EQUIOMENT_STATUS_USING);
        wxEquipmentService.upEquipment(equipment);
        int count = wxShareOrdersMapper.insertSelective(wxShareOrders);
        Map<String, Object> map = new HashMap<>();
        map.put("wxShareOrdersSub", wxShareOrdersMapper.getFrontShareOrderse(wxShareOrders.getShareOrderId()));
        if (count > 0) {
            return ResultView.ok(map);
        } else {
            return ResultView.error(ResultEnum.CODE_2);
        }

    }


    @Override
    public WxShareOrders selectByOutTradeNo(String outTradeNo) {
        return wxShareOrdersMapper.getByoutTradeNo(outTradeNo);
    }
}
