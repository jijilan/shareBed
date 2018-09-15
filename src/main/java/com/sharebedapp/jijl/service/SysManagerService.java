package com.sharebedapp.jijl.service;

import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.result.ResultView;

public interface SysManagerService {
    AuthorityManager backLogin(String userAcount, String passWord);

    int getByUserAcount(String userAcount);

    int addManager(AuthorityManager manager);

    int updateManagerPwd(AuthorityManager manager);




    int getCountByAccount(String account);

    ResultView getManagetList(Integer pageNo, Integer pageSize);

    ResultView delManageById(String managerId);

}
