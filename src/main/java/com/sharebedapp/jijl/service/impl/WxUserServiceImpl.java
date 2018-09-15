package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.WxCategoryMapper;
import com.sharebedapp.jijl.mapper.WxEquipmentMapper;
import com.sharebedapp.jijl.mapper.WxHospitalMapper;
import com.sharebedapp.jijl.mapper.WxUserMapper;
import com.sharebedapp.jijl.model.WxCategory;
import com.sharebedapp.jijl.model.WxEquipment;
import com.sharebedapp.jijl.model.WxHospital;
import com.sharebedapp.jijl.model.WxUser;
import com.sharebedapp.jijl.model.wrap.CategoryInfo;
import com.sharebedapp.jijl.model.wrap.EquipmentInfo;
import com.sharebedapp.jijl.result.ResultStatus;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.WxUserService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WxUserServiceImpl implements WxUserService {
    @Autowired
    private WxUserMapper wxUserMapper;

    @Autowired
    private WxCategoryMapper wxCategoryMapper;

    @Autowired
    private WxEquipmentMapper wxEquipmentMapper;

    @Autowired
    private WxHospitalMapper wxHospitalMapper;

    @Override
    public WxUser getByOpenId(String openId) {
        return  wxUserMapper.getByOpenId(openId);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertWxUser(WxUser wxUser) {
        return wxUserMapper.insertSelective(wxUser);
    }

    @Override
    public WxUser getByUserId(String userId) {
        return wxUserMapper.selectByPrimaryKey(userId);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWxUser(WxUser upWxUser) {
        return wxUserMapper.updateByPrimaryKeySelective(upWxUser);
    }

    @Override
    public ResultView getUserList(String nickName, String userPhone, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        PageInfo pageInfo=new PageInfo(wxUserMapper.getUserList(nickName,userPhone), pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo,"userList"));
    }

    @Override
    public ResultView getUser(String userId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("userInfo",  wxUserMapper.getUser(userId));
        return ResultView.ok(map);
    }

    @Override
    public WxUser getByUserPhone(String userPhone) {
        return wxUserMapper.getByUserPhone(userPhone);
    }

    @Override
    public HashMap<String, Object> getUserEquipmentList(Integer pageNo, Integer pageSize, String hospitalId, String userId) {
        HashMap<String, Object> map = new HashMap<>();

        WxEquipment wxEquipment = new WxEquipment();
        wxEquipment.setHospitalId(hospitalId);
        wxEquipment.setUserId(userId);
        List<EquipmentInfo> equipmentInfoList = new LinkedList<>();
        List<WxEquipment> wxEquipments = wxEquipmentMapper.selectByExample(wxEquipment);
        ArrayList<String> hospitalIds = new ArrayList<>();
        equipmentLoop:for (WxEquipment equipment : wxEquipments) {
            if (equipment != null) {
                EquipmentInfo equipmentInfo = new EquipmentInfo();
                WxHospital wxHospital = wxHospitalMapper.selectByPrimaryKey(equipment.getHospitalId());
                if (wxHospital != null) {
                    equipmentInfo.setHospitalName(wxHospital.getHospitalName());
                }
                WxCategory wxCategory = wxCategoryMapper.selectByPrimaryKey(equipment.getCategoryId());
                if (wxCategory == null){
                    continue equipmentLoop;
                }
                equipmentInfo.setCategoryName(wxCategory.getCategoryName());
                equipmentInfo.setEquipmentId(equipment.getEquipmentId());
                equipmentInfo.setEquipmentNumber(equipment.getEquipmentNumber());
                equipmentInfo.setEquipmentStatus(equipment.getEquipmentStatus());
                equipmentInfoList.add(equipmentInfo);
                if (!hospitalIds.contains(equipment.getHospitalId())) {
                    hospitalIds.add(equipment.getHospitalId());
                }
            }
        }
        map.put("equipmentInfoList",JsonUtils.PageInfoToMap(new PageInfo(equipmentInfoList,pageSize),"equipmentList"));

        List<CategoryInfo> categoryInfoList = new LinkedList<>();
        List<WxCategory> wxCategories = wxCategoryMapper.selectByExample(new WxCategory());
        categoryLoop:for (WxCategory wxCategory : wxCategories) {
            CategoryInfo categoryInfo = new CategoryInfo();
            if (wxCategory != null) {
                categoryInfo.setCategoryId(wxCategory.getCategoryId());
                categoryInfo.setCategoryName(wxCategory.getCategoryName());

                Integer total = 0;
                Integer failureQuantity = 0;
                for (String id : hospitalIds) {
                    if (id != null) {
                        total += wxEquipmentMapper.countByCategoryId(id, wxCategory.getCategoryId(), null, null, userId);
                        failureQuantity += wxEquipmentMapper.countByCategoryId(id, wxCategory.getCategoryId(), null, ResultStatus.EQUIOMENT_STATUS_FAILURE, userId);
                    }
                }
                if (total == 0){
                    continue categoryLoop;
                }
                categoryInfo.setTotal(total);
                categoryInfo.setFailureQuantity(failureQuantity);
                categoryInfoList.add(categoryInfo);
            }
        }
        map.put("categoryInfoList",categoryInfoList);

        return map;
    }

    @Override
    public WxUser getUserByPhone(String userPhone) {
        return wxUserMapper.getUserByPhone(userPhone);
    }

    @Override
    public int updatePhoneNumber(String userId, String oldPhoneNumber, String newPhoneNumber) {
        WxUser userByPhone = wxUserMapper.getUserByPhone(newPhoneNumber);
        if (userByPhone != null){
            return ResultStatus.PHONE_EXISTED;
        }
        WxUser wxUser = wxUserMapper.selectByPrimaryKey(userId);
        if (wxUser.getUserPhone()!=oldPhoneNumber){
            return -1;
        }
        wxUser.setUserPhone(newPhoneNumber);
        return wxUserMapper.updateByPrimaryKeySelective(wxUser);
    }

    @Override
    public WxUser getByOpenIdAnduserPhone(String openId, String userPhone) {
        return wxUserMapper.getByOpenIdAnduserPhone(openId,userPhone);
    }

    @Override
    public WxUser getByOpenIdAndiIsFlag(String openId, Integer [] isFlags) {

        return wxUserMapper.getByOpenIdAndiIsFlag(openId,isFlags);
    }

    @Override
    public void delUserByTaskUverdue() {
        wxUserMapper.delUserByTaskUverdue();
    }


}
