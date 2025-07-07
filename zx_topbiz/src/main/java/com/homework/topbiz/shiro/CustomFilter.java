package com.homework.topbiz.shiro;



import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomFilter extends BasicHttpAuthenticationFilter {
//public class CustomFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        log.debug("检查登录尝试，Authorization header: {}", authorization);
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        log.debug("执行登录，使用token: {}", authorization);

        CustomToken token = new CustomToken(authorization);
        try {
            getSubject(request, response).login(token);
            log.debug("登录成功");
            return true;
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            //throw new UnauthorizedException(e);
            return false;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("检查访问权限");
        if (isLoginAttempt(request, response)) {
            try {
                boolean success = executeLogin(request, response);
                log.debug("访问权限检查结果: {}", success);
                return success;
            } catch (Exception e) {
                log.error("访问权限检查失败: {}", e.getMessage());
                return false;
            }
        }
        log.warn("未找到Authorization header");
        return false;
    }



    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}