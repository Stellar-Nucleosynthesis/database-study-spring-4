package org.example.practice7.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserRateLimiter {
    private final StringRedisTemplate redisTemplate;

    private static final int limit = 5;
    private static final int windowSeconds = 10;

    private String key(String userId) {
        return "rate:user:" + userId;
    }

    public boolean allowRequest(String userId) {
        String key = key(userId);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }
        return count <= limit;
    }
}