package org.example.practice7.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.practice7.entity.UserProfile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserProfileRepository {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public UserProfileRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    private String key(String id) {
        return "user:" + id;
    }

    public void save(UserProfile profile) {
        try {
            String json = objectMapper.writeValueAsString(profile);
            redisTemplate.opsForValue().set(key(profile.getId()), json);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<UserProfile> findById(String id) {
        try {
            String json = redisTemplate.opsForValue().get(key(id));
            if (json == null) return Optional.empty();

            UserProfile profile = objectMapper.readValue(json, UserProfile.class);
            return Optional.of(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void update(UserProfile profile) {
        save(profile);
    }

    public void delete(String id) {
        redisTemplate.delete(key(id));
    }
}