package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.WxCategory;
import com.sharebedapp.jijl.result.ResultView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 9:31
 */
public interface WxCategoryService{
    int addCategory(WxCategory wxCategory, MultipartFile[] picture);

    int deleteCategory(String categoryId);

    ResultView getCategoryList(Integer pageNo, Integer pageSize);

    List<WxCategory> getCategorys();
}
