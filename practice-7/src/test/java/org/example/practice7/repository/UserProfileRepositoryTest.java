package org.example.practice7.repository;

import org.example.practice7.entity.UserProfile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserProfileRepositoryTest {
    static RedisServer redisServer;

    @Autowired
    UserProfileRepository repository;

    @BeforeAll
    static void startRedis() {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void stopRedis() {
        redisServer.stop();
    }

    @Test
    void create_and_read_profile() {
        UserProfile profile =
                new UserProfile("1", "Ivan", "ivan@test.com", 25);
        repository.save(profile);
        Optional<UserProfile> loaded = repository.findById("1");
        assertTrue(loaded.isPresent());
        assertEquals("Ivan", loaded.get().getName());
    }

    @Test
    void update_profile() {
        UserProfile profile =
                new UserProfile("2", "Petro", "p@test.com", 30);
        repository.save(profile);
        profile.setAge(31);
        repository.update(profile);
        Optional<UserProfile> updated = repository.findById("2");
        assertTrue(updated.isPresent());
        assertEquals(31, updated.get().getAge());
    }

    @Test
    void delete_profile() {
        UserProfile profile =
                new UserProfile("3", "Oleg", "o@test.com", 40);
        repository.save(profile);
        repository.delete("3");
        Optional<UserProfile> result = repository.findById("3");
        assertTrue(result.isEmpty());
    }
}