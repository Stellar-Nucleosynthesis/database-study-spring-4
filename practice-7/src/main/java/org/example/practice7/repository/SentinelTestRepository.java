package org.example.practice7.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SentinelTestRepository {
    private final StringRedisTemplate redisTemplate;

    public void save(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }
}