package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;

import com.sharebedapp.jijl.model.WxCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("wxCategoryMapper")
public interface WxCategoryMapper extends BaseMapper<WxCategory,String> {

    List<Map<String,Object>> getCategoryList();

    List<WxCategory> getCategorys();

    Integer getCategoryByName(String categoryName);
}
