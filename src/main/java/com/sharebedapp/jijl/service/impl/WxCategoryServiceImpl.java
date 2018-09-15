package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxCategoryMapper;
import com.sharebedapp.jijl.model.WxCategory;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCategoryService;
import com.sharebedapp.jijl.utils.IdentityUtil;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.sharebedapp.jijl.utils.UploadFileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author: jijl
 * @Date 2018/8/23 9:31
 */
@Service
public class WxCategoryServiceImpl implements WxCategoryService {
    @Autowired
    private WxCategoryMapper wxCategoryMapper;
    @Value("${web.portrait-path}")
    private String portraitPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addCategory(WxCategory wxCategory, MultipartFile[] picture) {
        if (wxCategoryMapper.getCategoryByName(wxCategory.getCategoryName()) >0 ){
            return ResultStatus.EXISTED;
        }

        wxCategory.setCategoryId(IdentityUtil.identityId("CA"));
        wxCategory.setCategoryPic(UploadFileUtil.flowUpload(picture,portraitPath,ResultStatus.CATEGORY_IMG_PATH));
        wxCategory.setcTime(new Date());
        return wxCategoryMapper.insertSelective(wxCategory);
    }

    @Override
    public int deleteCategory(String categoryId) {
        return wxCategoryMapper.deleteByPrimaryKey(categoryId);
    }

    @Override
    public ResultView getCategoryList(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(wxCategoryMapper.getCategoryList(), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"categoryList"));
    }

    @Override
    public List<WxCategory> getCategorys() {
        return wxCategoryMapper.getCategorys();
    }


}
