package com.example.demo.secruity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // 密钥，生产环境放到配置文件，至少256bit
    private final String SECRET_KEY = "mySuperSecretKey1234567890mySuperSecretKey1234567890";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 生成token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //1小时有效期
                .signWith(key)
                .compact();
    }

    // 从token提取用户名
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // 校验token
    public boolean validateToken(String token, String username) {
        String extractedUser = extractUsername(token);
        return (extractedUser.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration().before(new Date());
    }

    // 获取token剩余存活毫秒
    public long getTokenRemainExpireMs(String token){
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}

