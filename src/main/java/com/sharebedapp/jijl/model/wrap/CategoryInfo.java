package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

/**
 * @Author: jijl
 * @Date 2018/8/24 11:46
 */
@Data
public class CategoryInfo {
    private String categoryId;
    private String categoryName;
    private Integer total;
    private Integer availableQuantity;
    private Integer failureQuantity;
    private String categoryPic;
    private String categoryDesc;
    private double price;
}
