package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/27 21:03
 */

@Data
public class UserFinance {
    private double totalRevenue;
    private double withdrawAmount;
    private double frozenAmount;
    private double yesterdayRevenue;
    private List<UserFinanceInfo> userFinanceInfoList;
}
