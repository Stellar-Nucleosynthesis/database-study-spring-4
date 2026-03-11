package org.example.practice7.ratelimit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRateLimiterTest {
    static RedisServer redisServer;

    @Autowired
    UserRateLimiter rateLimiter;

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
    void allow_requests_under_limit() {
        String user = "user1";
        for (int i = 1; i <= 5; i++) {
            boolean allowed = rateLimiter.allowRequest(user);
            assertTrue(allowed);
        }
    }

    @Test
    void block_requests_over_limit() {
        String user = "user2";
        for (int i = 1; i <= 5; i++) {
            rateLimiter.allowRequest(user);
        }
        boolean blocked = rateLimiter.allowRequest(user);
        assertFalse(blocked);
    }

    @Test
    void allow_after_ttl_expires() throws InterruptedException {
        String user = "user3";
        for (int i = 0; i < 6; i++) {
            rateLimiter.allowRequest(user);
        }
        Thread.sleep(11000);
        boolean allowed = rateLimiter.allowRequest(user);
        assertTrue(allowed);
    }
}