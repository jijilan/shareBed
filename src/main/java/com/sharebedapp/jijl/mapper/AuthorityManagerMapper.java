package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.AuthorityManager;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AuthorityManagerMapper extends BaseMapper<AuthorityManager,String>{
    AuthorityManager backLogin(@Param(value="userAcount")String userAcount, @Param(value="passWord") String passWord);

    int getByUserAcount(@Param(value="userAcount")String userAcount);

    List<AuthorityManager> selectByManagerList();
}