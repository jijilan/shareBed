package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/29 16:58
 */

@Data
public class CashRequest {
    private double currentMonthWithdraw;
    private double currentQuarterWithdraw;
    private double currentYearWithdraw;
    private double agentWithdraw;
    private double buyerWithdraw;
    private List<AgentCashRequest> agentCashRequestList;
}
