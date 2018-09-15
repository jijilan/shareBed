package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: jijl
 * @Date 2018/8/27 14:13
 */
@Data
public class EquipmentRecord {
    private Date startTime;
    private String userAccount;
    private Integer operateType;
    private BigDecimal operateFee;
}
