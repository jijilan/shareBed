package com.sharebedapp.jijl.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Author: jijl
 * @Date 2018/9/10 0010 16:01
 */
public class DataConverter {
    public static double getTwoDigitNum(double revenue){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Double.valueOf(decimalFormat.format(revenue));
    }
    public static BigDecimal getTwoDigitNum(BigDecimal revenue){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return new BigDecimal(decimalFormat.format(revenue));
    }
}
