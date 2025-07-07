package com.homework.topbiz.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomToken implements AuthenticationToken {
    private String token;

    public CustomToken(String token) {
        this.token = token;
        log.info("创建 CustomToken: {}", token);
    }

    @Override
    public Object getPrincipal() {
        log.info("获取 Principal: {}", token);
        return token;
    }

    @Override
    public Object getCredentials() {
        log.info("获取 Credentials: {}", token);
        return token;
    }
} 