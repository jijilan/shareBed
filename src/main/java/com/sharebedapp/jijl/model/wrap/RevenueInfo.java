package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/29 14:01
 */
@Data
public class RevenueInfo {
    private double revenuePlatform;
    private double revenueAgent;
    private double revenueBuyer;
    private List<AgentRevenue> agentRevenueList;
}
