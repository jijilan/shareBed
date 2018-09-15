package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

/**
 * @Author: jijl
 * @Date 2018/9/4 0004 13:56
 */

@Data
public class GoodsOrdersInfo {
    private String hospitalName;
    private Integer orderStatus;
    private String categoryName;
    private Integer totalNum;
    private double totalPrice;
    private String goodsOrdersId;
    private String outTradeNo;
    private long payTime;
}
