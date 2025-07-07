package com.zxzy.zx_userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Service
public class JWTUtils implements Serializable {
    // 确保 serialVersionUID 以保证版本兼容性
    private static final long serialVersionUID = 1L;

    // 简化了的
    private final String pwd = "zxzy";
    private final long tokenTll = 1000 * 60 * 60 * 24 * 7; // 7天有效期

//    public String generateJwt(Map<String, Object> claims) {
//        String jwt = Jwts.builder()
//                .addClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, pwd)
//                .setExpiration(new Date(System.currentTimeMillis() + tokenTll))
//                .compact();
//        return jwt;
//    }
public String generateJwt(Map<String, Object> claims) {
    try {
        // 处理复杂对象，将其转换为可序列化的格式
        Map<String, Object> serializableClaims = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
                serializableClaims.put(key, value);
        }

        String jwt = Jwts.builder()
                .addClaims(serializableClaims)
                .signWith(SignatureAlgorithm.HS256, pwd)
                .setExpiration(new Date(System.currentTimeMillis() + tokenTll))
                .compact();
        return jwt;
    } catch (Exception e) {
        System.err.println("Error generating JWT: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(pwd)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}