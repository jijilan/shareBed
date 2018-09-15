package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

/**
 * @Author: jijl
 * @Date 2018/8/29 17:04
 */

@Data
public class AgentCashRequest {
    private String agentRoleId;
    private String agentRoleName;
    private double agentWithdraw;
}
