package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.util.Date;

/**
 * @Author: jijl
 * @Date 2018/8/27 9:23
 */
@Data
public class FinanceInfo {
    private String financeId;
    private String outTradeNo;
    private String username;
    private String userAccount;
    private String userPhone;
    private String userId;
    private String agentId;
    private String categoryName;
    private double payAmount;
    private Date cTime;
    private String hospitalName;
    private Integer num;
    private String dept;
    private String ward;
    private String bedNumber;
}
