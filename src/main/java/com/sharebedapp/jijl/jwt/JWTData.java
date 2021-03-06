package com.sharebedapp.jijl.jwt;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 * @Author: jijl
 * @Description: JWT加密参数类
 * @Date: 2018/7/2 16:52
 **/
@Data
@PropertySource(value = "classpath:/propertiess/jwt.properties")
@Component
public class JWTData {

    @Value("${audience.clientId}")
    private String clientId;
    @Value("${audience.base64Secret}")
    private String base64Secret;
    @Value("${audience.name}")
    private String name;
    @Value("${audience.expiresSecond}")
    private int expiresSecond;
}
