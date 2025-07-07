package com.homework.topbiz.controller;

import com.homework.topbiz.util.TokenUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 权限demo示例
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    private final RedisTemplate<String, Object> redisTemplate;

    public AuthController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        log.info("用户登录请求: username={}", username);
        // 这里应该添加实际的用户验证逻辑
        if ("admin".equals(username) && "password".equals(password)) {
            String token = TokenUtil.generateToken();
            log.info("生成token: {}", token);
            
            // 设置用户权限
            Set<String> permissions = new HashSet<>();
            permissions.add("user:read");
            permissions.add("user:edit");
            
            // 将权限存入 Redis
            String redisKey = "permissions:" + token;
            redisTemplate.opsForValue().set(redisKey, permissions, 60 * 60 * 24 * 7, TimeUnit.SECONDS);
            log.info("权限已存入Redis, key={}, value={}", redisKey, permissions);
            
            // 验证权限是否成功存入
            Set<String> savedPermissions = (Set<String>) redisTemplate.opsForValue().get(redisKey);
            log.info("验证Redis中的权限: {}", savedPermissions);
            
            return token;
        }
        log.warn("登录失败: username={}", username);
        return "登录失败";
    }

    @GetMapping("/test")
    @RequiresPermissions("user:read")
    public String test() {
        log.info("访问测试接口");
        return "您有权限访问此接口";
    }

    @GetMapping("/test_ubauth")
    @RequiresPermissions("user:viasdfsad")
    public String test_ubauth() {
        log.info("访问测试接口");
        return "您没有权限访问此接口";
    }
}