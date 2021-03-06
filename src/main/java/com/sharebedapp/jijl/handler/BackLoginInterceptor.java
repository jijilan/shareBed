package com.sharebedapp.jijl.handler;

import com.alibaba.druid.util.StringUtils;
import com.sharebedapp.jijl.exception.AuthException;
import com.sharebedapp.jijl.jwt.JWTData;
import com.sharebedapp.jijl.jwt.JwtUtil;
import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultStatus;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: jijl
 * @Description: 后台拦截器
 * @Date: 2018/7/2 16:50
 **/
@Slf4j
public class BackLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTData jwtData;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthException {
        //controller方法调用之前
        String token = request.getHeader(ResultStatus.TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new AuthException(ResultEnum.CODE_3);
        } else {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            jwtData = factory.getBean(JWTData.class);
            final Claims claims = JwtUtil.parseJWT(token, jwtData.getBase64Secret());
            try {
                String managerId = (String) claims.get(ResultStatus.MANAGER_ID);
                AuthorityManager manager = (AuthorityManager) redisService.get(managerId);
                if (manager != null) {
                    request.setAttribute(ResultStatus.MANAGER_ID, manager.getManagerId());
                    return true;
                }
            } catch (NullPointerException e) {
                throw new AuthException(ResultEnum.CODE_403);
            } catch (ClassCastException e) {
                throw new AuthException(ResultEnum.CODE_403);
            }catch (Exception e){
                throw new AuthException(ResultEnum.CODE_403);
            }
        }
        throw new AuthException(ResultEnum.CODE_403);
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
