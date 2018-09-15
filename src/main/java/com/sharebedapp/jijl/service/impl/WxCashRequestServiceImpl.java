package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.*;
import com.sharebedapp.jijl.model.*;
import com.sharebedapp.jijl.model.wrap.AgentCashRequest;
import com.sharebedapp.jijl.model.wrap.CashRequest;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCashRequestService;
import com.sharebedapp.jijl.utils.DateUtils;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: jijl
 * @Date 2018/8/28 11:23
 */

@Service
public class WxCashRequestServiceImpl implements WxCashRequestService {

    @Autowired
    private WxCashrequestMapper wxCashrequestMapper;

    @Autowired
    private WxBankcardsMapper wxBankcardsMapper;

    @Autowired
    private WxAgentMapper wxAgentMapper;

    @Autowired
    private WxRoleMapper wxRoleMapper;

    @Autowired
    private WxFinanceMapper wxFinanceMapper;

    @Autowired
    private WxSystemMapper wxSystemMapper;

    @Override
    public int withdrawDeposit(String consumerId, String bankCardId, double amount) {
        WxCashrequest wxCashrequest = new WxCashrequest();
        WxFinance wxFinance = new WxFinance();
        double totalwithdraw = 0;
        double frozen = 0;
        double passWithdraw = 0;
        double availableWithdraw;
        String userId = null;
        String agentId = null;
        if (consumerId.substring(0,2).equalsIgnoreCase("UI")){
            userId = consumerId;
            wxFinance.setUserId(consumerId);
            wxCashrequest.setUserId(consumerId);
        }
        if (consumerId.substring(0,2).equalsIgnoreCase("AG")){
            agentId = consumerId;
            wxCashrequest.setAgentId(consumerId);
            wxFinance.setAgentId(consumerId);
        }

        HashMap<String, Date> today = DateUtils.getLastDay(0);
        int countWithdraw = wxCashrequestMapper.countWithdraw(today.get("defaultStartDate"),today.get("defaultEndDate"), userId, agentId);

        if (countWithdraw>Integer.valueOf(wxSystemMapper.getSystem(ResultStatus.SYSTEMTYPE_WITHDRAW_AMOUNT).getParameter())){
            return ResultStatus.WITHDRAW_COUNT;
        }
        wxCashrequest.setCashRequestId(IdentityUtil.identityId("CR"));


        wxFinance.setFinanceType(ResultStatus.FINANCE_TYPE_EQUIP);
        wxFinance.setIsFlag(ResultStatus.ISFLAG_Y);
        List<WxFinance> withdrawFinanceList = wxFinanceMapper.selectByExample(wxFinance);
        for (WxFinance finance : withdrawFinanceList) {
            if (finance != null){
                totalwithdraw += finance.getRevenueAmount().doubleValue();
            }
        }

        WxCashrequest cashrequest = new WxCashrequest();
        if (consumerId.substring(0,2) == "UI"){
            cashrequest.setUserId(consumerId);
        }
        if (consumerId.substring(0,2) == "AG"){
            cashrequest.setAgentId(consumerId);
        }
        cashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_CHECKING);
        List<WxCashrequest> frozenCashrequests = wxCashrequestMapper.selectByExample(cashrequest);
        for (WxCashrequest frozenCashrequest : frozenCashrequests) {
            if (frozenCashrequest != null){
                frozen += frozenCashrequest.getAmount().doubleValue();
            }
        }

        cashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_PASS);
        List<WxCashrequest> passCashrequests = wxCashrequestMapper.selectByExample(cashrequest);
        for (WxCashrequest passCashrequest : passCashrequests) {
            if (passCashrequest != null){
                passWithdraw += passCashrequest.getAmount().doubleValue();
            }
        }

        availableWithdraw = totalwithdraw - frozen - passWithdraw;

        if (amount >= availableWithdraw){
            return ResultStatus.WITHDRAW_AMOUNT;
        }

        WxBankcards wxBankcards = wxBankcardsMapper.selectByPrimaryKey(bankCardId);
        wxCashrequest.setBankNumber(wxBankcards.getBankNumber());
        wxCashrequest.setBankRealName(wxBankcards.getBankRealName());
        wxCashrequest.setPhoneNumber(wxBankcards.getPhoneNumber());
        wxCashrequest.setBankName(wxBankcards.getBankName());
        wxCashrequest.setBankCardType(wxBankcards.getBankCardType());
        wxCashrequest.setCashRequestType(ResultStatus.CASH_REQUEST_TYPE_REVENUE);
        wxCashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_CHECKING);
        wxCashrequest.setcTime(new Date());

        WxFinance finance = new WxFinance();
        finance.setFinanceId(IdentityUtil.identityId("FI"));
        if (consumerId.substring(0,2) == "UI"){
            finance.setUserId(consumerId);
        }
        if (consumerId.substring(0,2) == "AG"){
            finance.setAgentId(consumerId);
        }
        finance.setFinanceType(ResultStatus.FINANCE_TYPE_WITHDRAM);
        finance.setIsFlag(ResultStatus.ISFLAG_Y);
        finance.setcTime(new Date());
        finance.setExpensesAmount(new BigDecimal(amount));
        wxFinanceMapper.insertSelective(finance);

        wxCashrequest.setAmount(new BigDecimal(amount));
        return wxCashrequestMapper.insertSelective(wxCashrequest);
    }

    @Override
    public ResultView getCashRequestList(Integer pageNo, Integer pageSize, String userPhone, String phoneNumber, String bankRealName) {
        PageHelper.startPage(pageNo,pageSize);
        List<Map<String, Object>> cashRequestList = wxCashrequestMapper.getCashRequestList(userPhone, phoneNumber, bankRealName);
        for (Map<String, Object> stringObjectMap : cashRequestList) {
            String isPurchaser = (String) stringObjectMap.get("isPurchaser");
            if("2".equals(isPurchaser)){
                stringObjectMap.put("roleName","认购商");
            }
        }
        PageInfo pageInfo=new PageInfo(cashRequestList, pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"cashRequestList"));
    }

    @Override
    public WxCashrequest getBycashRequestId(String cashRequestId) {
        return wxCashrequestMapper.selectByPrimaryKey(cashRequestId);
    }

    @Override
    public int updCashRequest(WxCashrequest wxCashrequest) {
        return wxCashrequestMapper.updateByPrimaryKeySelective(wxCashrequest);
    }

    @Override
    public CashRequest getCashRequest() {
        CashRequest cashRequest = new CashRequest();

        /**
         * 本月提现
         */
        double currentMonthWithdraw = 0;
        /**
         * 本季度提现
         */
        double currentQuarterWithdraw = 0;
        /**
         * 本年提现
         */
        double currentYearWithdraw = 0;
        /**
         * 渠道体现
         */
        double agentWithdraw = 0;
        /**
         * 购买商提现
         */
        double buyerWithdraw = 0;
        List<AgentCashRequest> agentCashRequestList = new LinkedList<>();

        WxRole role = new WxRole();
        List<WxRole> wxRoles = wxRoleMapper.selectByExample(role);
        for (WxRole wxRole : wxRoles) {
            AgentCashRequest agentCashRequest = new AgentCashRequest();
            agentCashRequest.setAgentRoleId(wxRole.getRoleId());
            agentCashRequest.setAgentRoleName(wxRole.getRoleName());
            agentCashRequestList.add(agentCashRequest);
        }

        WxCashrequest wxCashrequest = new WxCashrequest();
        wxCashrequest.setStatus(ResultStatus.CASH_REQUEST_STATUS_PASS);
        List<WxCashrequest> wxCashrequests = wxCashrequestMapper.selectByExample(wxCashrequest);

        double agenterWithdraw = 0;

        for (WxCashrequest cash : wxCashrequests) {
            /**
             * 渠道商提现计算
             */
            if (cash.getAgentId()!=null){
                agentWithdraw += cash.getAmount().doubleValue();
                WxAgent agent = wxAgentMapper.selectByPrimaryKey(cash.getAgentId());

                AgentCashRequest agentCash = new AgentCashRequest();
                agentCash.setAgentRoleId(agent.getRoleId());
                agentCash.setAgentWithdraw(cash.getAmount().doubleValue());

                for (AgentCashRequest agentCashRequest : agentCashRequestList) {
                    if (agentCashRequest.getAgentRoleId().equalsIgnoreCase(agent.getRoleId())){
                        agenterWithdraw += agentCash.getAgentWithdraw();
                        agentCashRequest.setAgentWithdraw(agenterWithdraw);
                    }
                }

            }
            if (cash.getUserId()!=null){
                buyerWithdraw += cash.getAmount().doubleValue();
            }

            /**
             * 月,季,年提现计算
             */
            Date currentMonthFirstDay = DateUtils.getDateTimeFirstDayCurrent();
            Date currentQuarterFirstDay = DateUtils.getCurrentQuarterStartTime();
            Date currentYearFirstDay = DateUtils.getCurrentYearStartTime();
            List<WxCashrequest> montCashRequest = wxCashrequestMapper.getCashRequestByStartTime(currentMonthFirstDay);
            for (WxCashrequest monthCashrequest : montCashRequest) {
                currentMonthWithdraw += monthCashrequest.getAmount().doubleValue();
            }

            List<WxCashrequest> quarterCashRequest = wxCashrequestMapper.getCashRequestByStartTime(currentQuarterFirstDay);
            for (WxCashrequest quarterCashrequest : quarterCashRequest) {
                currentQuarterWithdraw += quarterCashrequest.getAmount().doubleValue();
            }

            List<WxCashrequest> yearCashRequest = wxCashrequestMapper.getCashRequestByStartTime(currentYearFirstDay);
            for (WxCashrequest yearCashrequest : yearCashRequest) {
                currentYearWithdraw += yearCashrequest.getAmount().doubleValue();
            }
        }


        cashRequest.setAgentWithdraw(agentWithdraw);
        cashRequest.setBuyerWithdraw(buyerWithdraw);
        cashRequest.setAgentCashRequestList(agentCashRequestList);
        cashRequest.setCurrentMonthWithdraw(currentMonthWithdraw);
        cashRequest.setCurrentQuarterWithdraw(currentQuarterWithdraw);
        cashRequest.setCurrentYearWithdraw(currentYearWithdraw);

        return cashRequest;
    }
}
