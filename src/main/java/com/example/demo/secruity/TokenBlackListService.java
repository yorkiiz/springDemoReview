package com.example.demo.secruity;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListService {

    private final StringRedisTemplate stringRedisTemplate;

    public TokenBlackListService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * token加入黑名单
     */
    public void addBlackList(String token, long remainMs){
        if(remainMs > 0){
            stringRedisTemplate.opsForValue().set("jwt:black:" + token, "1", remainMs, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 判断是否黑名单
     */
    public boolean isInBlackList(String token){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey("jwt:black:" + token));
    }
}
