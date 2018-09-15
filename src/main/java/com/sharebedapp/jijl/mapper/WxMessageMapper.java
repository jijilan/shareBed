package com.sharebedapp.jijl.mapper;

import com.sharebedapp.jijl.mapper.base.BaseMapper;
import com.sharebedapp.jijl.model.WxMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("wxMessageMapper")
public interface WxMessageMapper extends BaseMapper<WxMessage,String> {

    List<WxMessage> selectByCondition(@Param("title") String title,
                                      @Param("start") Date start,
                                      @Param("end") Date end);
}