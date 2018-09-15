package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.*;
import com.sharebedapp.jijl.model.wrap.*;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxFinanceService;
import com.sharebedapp.jijl.utils.DataConverter;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.liujiebang.pay.wx.config.WxRequest;
import com.github.liujiebang.pay.wx.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class WxFinanceServiceImpl implements WxFinanceService {

    @Autowired
    private WxFinanceMapper wxFinanceMapper;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WxUserMapper wxUserMapper;

    @Autowired
    private WxAgentMapper wxAgentMapper;

    @Autowired
    private WxShareOrdersMapper wxShareOrdersMapper;

    @Autowired
    private WxGoodsOrdersMapper wxGoodsOrdersMapper;

    @Autowired
    private WxSystemMapper wxSystemMapper;

    @Autowired
    private WxCashrequestMapper wxCashrequestMapper;

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;

    @Autowired
    private WxHospitalMapper wxHospitalMapper;

    @Autowired
    private WxRoleMapper wxRoleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView payForBail(String userId, String price) {
        WxFinance wxFinance=new WxFinance();
        wxFinance.setFinanceId(IdentityUtil.identityId("FI"));
        wxFinance.setUserId(userId);
        WxSystem system = wxSystemMapper.getSystem(ResultStatus.SYSTEMTYPE_1);
        String parameter = system.getParameter();
        if(!parameter.equals(price)){
            return  ResultView.error(ResultEnum.CODE_173);
        }
        wxFinance.setBalance(BigDecimal.valueOf(Double.valueOf(price)));
        wxFinance.setOutTradeNo(IdentityUtil.identityId("BAI"));
        wxFinance.setFinanceType(ResultStatus.WXFINACE_FINANCEETYPE_5);
        wxFinance.setBalanceType(ResultStatus.WXFINACE_BALANCETYPE_2);
        wxFinance.setIsFlag(ResultStatus.ISFLAG_N);
        wxFinance.setcTime(new Date());
        if(wxFinanceMapper.insertSelective(wxFinance)>0) {
            String outTradeNo = wxFinance.getOutTradeNo();
            HashMap<String,String> map=new HashMap<String,String>(1);
            map.put("outTradeNo",outTradeNo);
            return ResultView.ok(map);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payCallback(String outTradeNo, Integer payType) {
        WxFinance wxFinance = wxFinanceMapper.selectByOutTradeNo(outTradeNo);

        if(wxFinance.getIsFlag()!=ResultStatus.ISFLAG_Y){
            wxFinance.setPayType(payType);
            wxFinance.setIsFlag(ResultStatus.ISFLAG_Y);
            wxFinanceMapper.updateByPrimaryKeySelective(wxFinance);
        }

    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView backBail(String userId) {
        //查询是否有未支付，或进行中的订单 有就不能退押金
      if (wxShareOrdersMapper.getByUserId(userId).size() > 0) {
            return ResultView.error(ResultEnum.CODE_211);
        }
        WxFinance finance=wxFinanceMapper.balance(userId,ResultStatus.WXFINACE_BALANCETYPE_2);
        if(finance==null){
            return ResultView.error(ResultEnum.CODE_220);
        }else {
            if (finance.getFinanceType() != ResultStatus.WXFINACE_FINANCEETYPE_5) {
                return ResultView.error(ResultEnum.CODE_220);
            }else{                                                     //  finance.getBalance().doubleValue()/100
               boolean bool = wxPayService.wxReturn(finance.getOutTradeNo(),0.01, 0.01,WxRequest.WX_SP_PAY);
                if(bool){
                    //生成财务明细
                    WxFinance wxFinance=new WxFinance();
                    wxFinance.setFinanceId(IdentityUtil.identityId("FI"));
                    wxFinance.setUserId(userId);

                    wxFinance.setBalance(finance.getBalance());

                    wxFinance.setOutTradeNo(IdentityUtil.identityId("BAI"));
                    wxFinance.setFinanceType(ResultStatus.WXFINACE_FINANCEETYPE_6);
                    wxFinance.setBalanceType(ResultStatus.WXFINACE_BALANCETYPE_2);

                    wxFinance.setPayType(ResultStatus.PAYTYPE_WX);
                    wxFinance.setIsFlag(ResultStatus.ISFLAG_Y);
                    wxFinance.setcTime(new Date());
                    wxFinanceMapper.insertSelective(wxFinance);
                    return  ResultView.ok();
                }
            }
        }
        return ResultView.error(ResultEnum.CODE_2);
    }
    @Override
    public WxFinance isBail(String userId) {
        return wxFinanceMapper.balance(userId,ResultStatus.WXFINACE_BALANCETYPE_2);
    }

    @Override
    public WxFinance getByAgentId(String agentId) {
        return wxFinanceMapper.getByAgentId(agentId);
    }

    @Override
    public UserFinance getFinanceByUser(String userId, String agentId, Integer days, String startTime, String endTime, String hospitalId) {
        UserFinance userFinance = new UserFinance();
        //总收益
        double totalRevenue = 0;
        //总可提现金额
        double withdrawAmount;
        //冻结金额
        double frozenAmount = 0;
        //已提现金额
        double withdrawPassAmount = 0 ;
        //昨日收益
        double yesterdayRevenue = 0;

        if (userId == null && agentId == null){
            return null;
        }

        //购买商或渠道商的收益查询
        WxFinance wxFinance = new WxFinance();
        if (userId != null){
            wxFinance.setUserId(userId);
        }
        if (agentId != null) {
            wxFinance.setAgentId(agentId);
        }
        wxFinance.setIsFlag(ResultStatus.ISFLAG_Y);
        wxFinance.setFinanceType(ResultStatus.FINANCE_TYPE_EQUIP);

        //查询对应用户的财务列表
        List<WxFinance> wxFinances = wxFinanceMapper.selectByExample(wxFinance);
        for (WxFinance finance : wxFinances) {
            UserFinanceInfo userFinanceInfo = new UserFinanceInfo();
            userFinanceInfo.setFee(finance.getRevenueAmount().doubleValue());
            totalRevenue += userFinanceInfo.getFee();
        }
        userFinance.setTotalRevenue(DataConverter.getTwoDigitNum(totalRevenue));

        HashMap<String, Date> lastDay = DateUtils.getLastDay(1);
        List<WxFinance> wxFinancesYesterday = wxFinanceMapper.selectByCTime(lastDay.get("defaultStartDate"),lastDay.get("defaultEndDate"),userId , agentId);

        for (WxFinance finance : wxFinancesYesterday) {
            if (finance!=null) {
                yesterdayRevenue += finance.getRevenueAmount().doubleValue();
            }
        }

        userFinance.setYesterdayRevenue(DataConverter.getTwoDigitNum(yesterdayRevenue));

        //查询用户的提现记录

        WxCashrequest wxCashrequest = new WxCashrequest();
        if (agentId != null){
            wxCashrequest.setAgentId(agentId);
        }
        if (userId != null){
            wxCashrequest.setUserId(userId);
        }
        wxCashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_CHECKING);
        List<WxCashrequest> checkingCashrequests = wxCashrequestMapper.selectByExample(wxCashrequest);
        for (WxCashrequest checkingCashrequest : checkingCashrequests) {
            frozenAmount += checkingCashrequest.getAmount().doubleValue();
        }
        userFinance.setFrozenAmount(DataConverter.getTwoDigitNum(frozenAmount));
        wxCashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_PASS);
        List<WxCashrequest> passCashrequests = wxCashrequestMapper.selectByExample(wxCashrequest);
        for (WxCashrequest passCashrequest : passCashrequests) {
            withdrawPassAmount += passCashrequest.getAmount().doubleValue();
        }
        withdrawAmount = totalRevenue - frozenAmount - withdrawPassAmount;
        userFinance.setWithdrawAmount(DataConverter.getTwoDigitNum(withdrawAmount));

        //查询用户所有的设备列表
        List<WxEquipment> wxEquipmentList = new LinkedList<>();
        if (agentId != null){
            WxAgent wxAgent = wxAgentMapper.selectByPrimaryKey(agentId);
            if (wxAgent!=null) {
                wxEquipmentList = wxEquipmentMapper.selectByHospitalId(wxAgent.getHospitalId());
            }
        }
        if (userId != null){
            WxEquipment wxEquipment = new WxEquipment();
            wxEquipment.setUserId(userId);
            if (!"".equals(hospitalId)){
                wxEquipment.setHospitalId(hospitalId);
            }
            wxEquipmentList = wxEquipmentMapper.selectByExample(wxEquipment);
        }

        //用户所属设备的共享订单信息
        userFinance.setUserFinanceInfoList(getUserFinanceInfoList(wxEquipmentList, days, startTime, endTime, hospitalId, userId, agentId));

        return userFinance;
    }

    /**
     * 通过设备列表查询对应的共享订单的使用信息
     * @param wxEquipments 设备列表
     * @param days 要查询的历史天数
     * @param start 筛选的开始时间
     * @param end 筛选的结束时间
     * @param hospitalId 筛选的医院编号
     * @param userId 查询的购买商的编号
     * @param agentId 查询的渠道商编号
     * @return 订单使用信息列表
     */
    private List<UserFinanceInfo> getUserFinanceInfoList(List<WxEquipment> wxEquipments, Integer days, String start, String end, String hospitalId, String userId, String agentId){
        List<UserFinanceInfo> userFinanceInfoList = new LinkedList<>();
        Date startTime = new Date();
        Date endTime = new Date();

        if (days!=null){
            HashMap<String, Date> lastDay = DateUtils.getLastDay(days);
            if (days==1){
                startTime = lastDay.get("defaultStartDate");
                endTime = lastDay.get("defaultEndDate");
            }else {
                startTime = lastDay.get("defaultStartDate");
                endTime = new Date();
            }
        }
        if (days == null && "".equals(start) && "".equals(end)){
            startTime = DateUtils.getTimeFromLong(0);
            endTime = new Date();
        }
        if(!"".equals(start)) {
            startTime = DateUtils.stringToDate(start+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        }
        if (!"".equals(end)){
            endTime = DateUtils.stringToDate(end+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        }

        for (WxEquipment equipment : wxEquipments) {

            List<WxShareOrders> wxShareOrdersList = new LinkedList<>();

            if (agentId != null){
                wxShareOrdersList = wxShareOrdersMapper.selectByCondition(startTime, endTime, wxAgentMapper.selectByPrimaryKey(agentId).getHospitalId());
            }
            if (userId != null) {
                wxShareOrdersList = wxShareOrdersMapper.selectByEquipmentId(equipment.getEquipmentId(), startTime, endTime, hospitalId);
            }
            loop:for (WxShareOrders wxShareOrders : wxShareOrdersList) {
                if(wxShareOrders != null) {
                    UserFinanceInfo userFinanceInfo = new UserFinanceInfo();
                    userFinanceInfo.setFinanceId(wxShareOrders.getShareOrderId());
                    String outTradeNumber = wxShareOrders.getOutTradeNo();
                    WxFinance revenueFinance = wxFinanceMapper.selectByOutTradeNoAndConsumerId(outTradeNumber,userId,agentId);
                    if (revenueFinance != null) {
                        userFinanceInfo.setFee(revenueFinance.getRevenueAmount().doubleValue());
                    }
                    if (wxShareOrders.getPayTime() == null){
                        continue loop;
                    }
                    userFinanceInfo.setChargeTime(wxShareOrders.getLeaseEndTime().getTime() - wxShareOrders.getLeaseStartTime().getTime());
                    SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
                    String format = df.format(wxShareOrders.getcTime());
                    userFinanceInfo.setDate(format.substring(5, 11).replace("-", "月").replace(" ", "日"));
                    userFinanceInfo.setTime(format.substring(11, 16));
                    userFinanceInfoList.add(userFinanceInfo);
                }
            }
        }
        return userFinanceInfoList;
    }

    @Override
    public ResultView getFinanceBailList(String userPhone, String nickName, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo<java.util.Map<String, Object>> pageInfo=new PageInfo<>(wxFinanceMapper.getFinanceBailList(userPhone,nickName),pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"financeList"));
    }

    @Override
    public RevenueInfo getRevenueInfo() {
        long start = System.currentTimeMillis();

        RevenueInfo revenueInfo = new RevenueInfo();
        //平台总收入
        double revenuePlatform = 0;
        //购买收益
        double revenuePlatformGood = 0;
        //设备购买收益
        BigDecimal platformGoodRevenue = wxFinanceMapper.calculateRevenuePlatformGood();
        if (platformGoodRevenue != null) {
            revenuePlatformGood = platformGoodRevenue.doubleValue();
        }
        //所有共享订单收入
        double totalShareRevenue = 0;
        BigDecimal shareRevenue = wxShareOrdersMapper.calculateShareRevenue();
        if (shareRevenue != null) {
            totalShareRevenue = shareRevenue.doubleValue();
        }
        //所有渠道商收入
        double revenueAgent = 0;
        BigDecimal agentRevenue = wxFinanceMapper.calculateAgentRevenue();
        if (agentRevenue != null){
            revenueAgent = agentRevenue.doubleValue();
        }
        //所有购买商收入
        BigDecimal buyerRevenue = wxFinanceMapper.calculateBuyerRevenue();
        double revenueBuyer = 0;
        if (buyerRevenue != null){
            revenueBuyer = buyerRevenue.doubleValue();
        }
        //平台通过共享订单获得的收入
        double platformShareRevenue = totalShareRevenue - revenueAgent -revenueBuyer;
        //平台总收入
        revenuePlatform = platformShareRevenue + revenuePlatformGood;
        //平台个角色收益
        List<AgentRevenue> agentRevenueList = wxFinanceMapper.calculateRoleRevenue();

        revenueInfo.setRevenueBuyer(DataConverter.getTwoDigitNum(revenueBuyer));
        revenueInfo.setRevenueAgent(DataConverter.getTwoDigitNum(revenueAgent));
        revenueInfo.setRevenuePlatform(DataConverter.getTwoDigitNum(revenuePlatform));
        revenueInfo.setAgentRevenueList(agentRevenueList);

        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("收益计算所需时间:" + time +"毫秒");
        return revenueInfo;
    }


    @Override
    public PageInfo getFinanceList(Integer pageNo, Integer pageSize, String hospitalName, String categoryId, Integer financeType) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo<FinanceInfo> pageInfo;
        //获取财务列表
        List<FinanceInfo> financeInfoList = wxFinanceMapper.getFinanceList(hospitalName,categoryId,financeType);
        for (FinanceInfo financeInfo : financeInfoList) {
            if (!StringUtils.isEmpty(financeInfo.getUserId())){
                WxUser user = wxUserMapper.selectByPrimaryKey(financeInfo.getUserId());
                if (user!=null) {
                    financeInfo.setUsername(user.getNickName());
                    financeInfo.setUserPhone(user.getUserPhone());
                }
            }
            if (!StringUtils.isEmpty(financeInfo.getAgentId())) {
                WxAgent agent = wxAgentMapper.selectByPrimaryKey(financeInfo.getAgentId());
                if (agent!=null) {
                    financeInfo.setUserAccount(agent.getAgentAccount());
                    financeInfo.setUsername(agent.getAgentNickName());
                    financeInfo.setUserPhone(agent.getAgentPhone());
                }
            }

            //查询共享订单信息列表
            WxShareOrders wxShareOrders = wxShareOrdersMapper.selectByOutTradeNo(financeInfo.getOutTradeNo());
            if (wxShareOrders !=null){
                financeInfo.setCTime(wxShareOrders.getcTime());
                financeInfo.setPayAmount(wxShareOrders.getPrice().doubleValue());
            }

            //查询购买订单列表
            WxGoodsOrders wxGoodsOrders = wxGoodsOrdersMapper.selectByOutTradeNo(financeInfo.getOutTradeNo());
            if (wxGoodsOrders != null){
                financeInfo.setCTime(wxGoodsOrders.getcTime());
                financeInfo.setNum(wxGoodsOrders.getTotalNum());
                financeInfo.setPayAmount(wxGoodsOrders.getTotalPrice().doubleValue());
            }
        }

        pageInfo = new PageInfo<>(financeInfoList, pageSize);
        return pageInfo;
    }
}
