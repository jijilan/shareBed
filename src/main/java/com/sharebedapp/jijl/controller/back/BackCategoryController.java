package com.sharebedapp.jijl.controller.back;

import com.sharebedapp.jijl.model.WxCategory;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxCategoryService;
import com.sharebedapp.jijl.service.WxEquipmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: jijl
 * @Date 2018/8/23 9:30
 */
@RestController
@RequestMapping("/back")
public class BackCategoryController {
    private final WxCategoryService wxCategoryService;
    private final WxEquipmentService wxEquipmentService;

    @Autowired
    public BackCategoryController(WxCategoryService wxCategoryService, WxEquipmentService wxEquipmentService) {
        this.wxCategoryService = wxCategoryService;
        this.wxEquipmentService = wxEquipmentService;
    }


    /***
     * 设备分类列表
    * @param pageNo 页数
  * @param pageSize 页数大小
     * @return
     */
    @GetMapping("/getCategoryList")
    public ResultView getCategoryList(@RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                      @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        return wxCategoryService.getCategoryList(pageNo,pageSize);
    }
    /***
     * 添加设备分类
     *  categoryName 设备分类名称
     *  categoryDesc 设备分类图片
     *  picture 设备类型
     * @return
     */
    @PostMapping("/addCategory")
    public ResultView addCategory(WxCategory wxCategory,
                                  @RequestParam(value = "picture",required = false) MultipartFile[] picture){

        if (StringUtils.isEmpty(wxCategory.getCategoryName())){
            return ResultView.error(ResultEnum.CODE_22);
        }
        if(StringUtils.isEmpty(wxCategory.getCategoryDesc())){
            return ResultView.error(ResultEnum.CODE_23);
        }

        int flag = wxCategoryService.addCategory(wxCategory,picture);

        if (flag > 0){
            return ResultView.ok();
        }else if (flag == ResultStatus.EXISTED){
            return ResultView.error(ResultEnum.CODE_65);
        }
        return ResultView.error(ResultEnum.CODE_2);
    }


    /***
     * 删除设备分类设备
     * @param categoryId 设备分类编号
     * @return
     */
    @PostMapping("/deleteCategory")
    public ResultView deleteCategory(String categoryId){
        if(wxEquipmentService.getByCategoryId(categoryId)){
            return ResultView.error(ResultEnum.CODE_188);
        }
        if (categoryId == null){
            return ResultView.error(ResultEnum.CODE_24);
        }
        if (wxCategoryService.deleteCategory(categoryId)>0){
            return ResultView.ok();
        }
        return ResultView.error(ResultEnum.CODE_25);
    }
}
