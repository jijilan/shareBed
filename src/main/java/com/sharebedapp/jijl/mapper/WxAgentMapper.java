package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxAgent;
import com.sharebedapp.jijl.model.wrap.AgentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("wxAgentMapper")
public interface WxAgentMapper extends BaseMapper<WxAgent,String> {

    List<AgentInfo> selectAgentByCondition(@Param("roleName") String roleName,
                                           @Param("agentAccount") String agentAccount,
                                           @Param("agentNickName") String agentNickName,
                                           @Param("proportion") Integer proportion,
                                           @Param("agentPhone") String agentPhone,
                                           @Param("hospitalId") String hospitalId);

    List<WxAgent> getEarningsAgetnList(@Param("hospitalId")String hospitalId);

    WxAgent selectByAgentPhone(String agentPhone);

    List<WxAgent> selectByHospitalId(String hospitalId);

    WxAgent selectAgentByAccount(String agentAccount);
}