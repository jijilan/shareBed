package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

/**
 * @Author: jijl
 * @Date 2018/8/27 21:06
 */
@Data
public class UserFinanceInfo {
    private String date;
    private String time;
    private String financeId;
    private double fee;
    private long chargeTime;
}
