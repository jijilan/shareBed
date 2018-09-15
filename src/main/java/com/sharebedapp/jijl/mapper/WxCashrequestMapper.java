package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxCashrequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("wxCashrequestMapper")
public interface WxCashrequestMapper extends BaseMapper<WxCashrequest,String>{

    List<Map<String,Object>> getCashRequestList(@Param("userPhone") String userPhone,
                                                @Param("phoneNumber") String phoneNumber,
                                                @Param("bankRealName")String bankRealName);

    List<WxCashrequest> getCashRequestByStartTime(Date startTime);

    int countWithdraw(@Param("startTime") Date startTime,
                      @Param("endTime") Date endTime ,
                      @Param("userId") String userId ,
                      @Param("agentId") String agentId);
}