package com.sharebedapp.jijl.service.impl;

import com.sharebedapp.jijl.mapper.AuthorityManagerMapper;
import com.sharebedapp.jijl.mapper.AuthorityRoleMapper;
import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultView;
import com.sharebedapp.jijl.service.SysManagerService;
import com.sharebedapp.jijl.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysManagerServiceImpl implements SysManagerService {
    @Autowired
    private AuthorityManagerMapper authorityManagerMapper;
    @Autowired
    private AuthorityRoleMapper authorityRoleMapper;
    @Autowired
    private RedisService redisService;
    @Override
    public AuthorityManager backLogin(String userAcount, String passWord) {
        return authorityManagerMapper.backLogin(userAcount,passWord);
    }

    @Override
    public int getByUserAcount(String userAcount) {
        return authorityManagerMapper.getByUserAcount(userAcount);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addManager(AuthorityManager manager) {
        return authorityManagerMapper.insertSelective(manager);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateManagerPwd(AuthorityManager manager) {
        return authorityManagerMapper.updateByPrimaryKeySelective(manager);
    }


    @Override
    public int getCountByAccount(String account) {
        AuthorityManager authorityManager = new AuthorityManager();
        authorityManager.setUserAcount(account);
        List<AuthorityManager> managerList = authorityManagerMapper.selectByExample(authorityManager);
        if (managerList == null && managerList.size() == 0){
            return 0;
        }
        return managerList.size();
    }

    @Override
    public ResultView getManagetList(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<AuthorityManager> list = authorityManagerMapper.selectByManagerList();
        PageInfo pageInfo = new PageInfo(list, pageSize);
        return ResultView.ok(JsonUtils.PageInfoToMap(pageInfo, "list"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultView delManageById(String managerId) {
        return (authorityRoleMapper.delRoleByManager(managerId) >= 0 && authorityManagerMapper.deleteByPrimaryKey(managerId) >= 0) ? ResultView.ok() : ResultView.error(ResultEnum.CODE_2);
    }
}
