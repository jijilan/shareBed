package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/9/1 0001 10:02
 */
@Data
public class RechargeInfo {
    private Integer rechargeType;
    private Integer minHour;
    private BigDecimal overtimePrice;
    private List<TimeRechargeInfo> timeRechargeInfoList;
}
