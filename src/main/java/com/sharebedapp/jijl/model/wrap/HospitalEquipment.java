package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 20:53
 */
@Data
public class HospitalEquipment {
    private String hospitalId;
    private String hospitalName;
    private String hospitalCity;
    private String hospitalAddress;
    private String hospitalPic;
    private List<CategoryInfo> categoryInfoList;
}
