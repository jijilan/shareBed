package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component("wxUserMapper")
public interface WxUserMapper extends BaseMapper<WxUser,String> {

    WxUser getByOpenId(String openId);

    List<Map<String,Object>> getUserList(@Param("nickName") String nickName,
                                         @Param("userPhone") String userPhone);

    Map<String,Object> getUser(String userId);

    WxUser getByUserPhone(String userPhone);

    WxUser getUserByPhone(String userPhone);

    WxUser getByOpenIdAnduserPhone(@Param("openId")String openId,@Param("userPhone") String userPhone);

    WxUser getByOpenIdAndiIsFlag(@Param("openId")String openId,@Param("isFlags")Integer[] isFlags);

    Integer delUserByTaskUverdue();
}