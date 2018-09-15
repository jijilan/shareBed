package com.sharebedapp.jijl.handler;

import com.sharebedapp.jijl.exception.AuthException;
import com.sharebedapp.jijl.model.AuthorityManager;
import com.sharebedapp.jijl.redis.RedisService;
import com.sharebedapp.jijl.result.ResultEnum;
import com.sharebedapp.jijl.result.ResultStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @auther: jijl
 * @Date: Create in 2018/8/14
 * @Description:
 **/
public class BackRoleInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisService redisService;

    /**
     * @Description 权限拦截
     * @Date 2018/8/20 21:35
     * @Author liangshihao
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestURI = request.getRequestURI();
        if ("/back/authority/getAuthorityMenuList".equals(requestURI) ||
                "/back/updateManagerPwd".equals(requestURI)||
                "/back/managerInfo".equals(requestURI)||
                "/back/getExitLogin".equals(requestURI)){
            return true;
        }


        String managerId = (String) request.getAttribute(ResultStatus.MANAGER_ID);
        AuthorityManager manager = (AuthorityManager) redisService.get(managerId);
        //超级管理员直接放行
        if (manager != null && manager.getManagerType() == ResultStatus.MANAGERTYPE_1) {
            return true;
        }
        List<String> authorityList = (List<String>) redisService.get(ResultStatus.AUTHORITY + managerId);
        if (authorityList.contains(requestURI)) {
            return true;
        } else {
            throw new AuthException(ResultEnum.CODE_181);
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
