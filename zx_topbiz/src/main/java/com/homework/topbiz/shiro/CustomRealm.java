package com.homework.topbiz.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {

    private final RedisTemplate<String, Object> redisTemplate;

    public CustomRealm(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        log.debug("检查是否支持 token 类型: {}", token.getClass().getName());
        return token instanceof CustomToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) principals.getPrimaryPrincipal();
        log.debug("开始授权，token: {}", token);
        
        String redisKey = "permissions:" + token;
        log.debug("从Redis获取权限，key: {}", redisKey);
        
        Set<String> permissions = (Set<String>) redisTemplate.opsForValue().get(redisKey);
        log.debug("从Redis获取到的权限: {}", permissions);
        
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (permissions != null) {
            authorizationInfo.setStringPermissions(permissions);
            log.debug("设置权限到AuthorizationInfo: {}", permissions);
        } else {
            log.warn("未找到权限信息");
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String tokenStr = (String) token.getPrincipal();
        log.debug("开始认证，token: {}", tokenStr);
        
        String redisKey = "permissions:" + tokenStr;
        log.debug("检查Redis中是否存在权限，key: {}", redisKey);
        
        boolean hasKey = redisTemplate.hasKey(redisKey);
        log.debug("Redis中是否存在该token的权限: {}", hasKey);
        
        if (hasKey) {
            log.debug("认证成功");
            return new SimpleAuthenticationInfo(tokenStr, tokenStr, getName());
        }
        log.warn("认证失败：未找到token对应的权限信息");
        return null;
    }
} 