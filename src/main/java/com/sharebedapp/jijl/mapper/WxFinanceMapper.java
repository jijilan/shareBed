package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxFinance;

import com.sharebedapp.jijl.model.wrap.AgentRevenue;
import com.sharebedapp.jijl.model.wrap.FinanceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("wxFinanceMapper")
public interface WxFinanceMapper extends BaseMapper<WxFinance,String> {

    WxFinance selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    WxFinance selectByOutTradeNoAndConsumerId(@Param("outTradeNo") String outTradeNo,
                                 @Param("userId") String userId,
                                 @Param("agentId") String agentId);

    WxFinance balance(@Param(value="userId")String userId,
                      @Param(value="balanceType") int balanceType);

    List<FinanceInfo> getFinanceList(@Param("hospitalName") String hospitalName,
                                     @Param("categoryId") String categoryId,
                                     @Param("financeType") Integer financeType);


    WxFinance getByAgentId(@Param("agentId")String agentId);

    List<Map<String,Object>> getFinanceBailList(@Param("userPhone")String userPhone,
                                                @Param("nickName") String nickName);

    List<WxFinance> selectByCTime(@Param("start") Date start,
                                  @Param("end") Date end,
                                  @Param("userId") String userId,
                                  @Param("agentId") String agentId);

    BigDecimal calculateRevenuePlatformGood();

    BigDecimal calculateAgentRevenue();

    BigDecimal calculateBuyerRevenue();

    List<AgentRevenue> calculateRoleRevenue();

    BigDecimal calculateRevenueByAgentId(@Param("agentId") String agentId);
}