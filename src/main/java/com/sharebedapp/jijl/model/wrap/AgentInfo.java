package com.sharebedapp.jijl.model.wrap;

import lombok.Data;

/**
 * @Author: jijl
 * @Date 2018/8/23 19:09
 */

@Data
public class AgentInfo {
    private String agentId;
    private String agentAccount;
    private String agentPassWord;
    private String agentNickName;
    private String agentPhone;
    private String hospitalName;
    private String hospitalId;
    private String hospitalAddress;
    private String roleName;
    private Integer proportion;
    private double revenue;
    private Integer isFlag;
}
