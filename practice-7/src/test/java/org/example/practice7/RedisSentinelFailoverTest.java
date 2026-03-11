package org.example.practice7;

import org.example.practice7.repository.SentinelTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisSentinelFailoverTest {
    @Autowired
    private SentinelTestRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        Runtime.getRuntime().exec("docker start redis-master");
        Thread.sleep(10000);
    }

    @Test
    void should_read_and_write_when_master_is_up() {
        repository.save("key1", "value1");
        assertEquals("value1", repository.get("key1"));
    }

    @Test
    void should_work_after_master_failover() throws Exception {
        repository.save("before_failover", "value1");
        assertEquals("value1", repository.get("before_failover"));
        Runtime.getRuntime().exec("docker kill redis-master");
        Thread.sleep(30000);
        repository.save("after_failover", "value2");
        assertEquals("value2", repository.get("after_failover"));
    }

    @Test
    void data_written_before_failover_should_be_readable_after() throws Exception {
        repository.save("persistent_key", "persistent_value");
        Runtime.getRuntime().exec("docker kill redis-master");
        Thread.sleep(30000);
        assertEquals("persistent_value", repository.get("persistent_key"));
    }

    @Test
    void should_recover_when_old_master_rejoins() throws Exception {
        repository.save("key_before", "value_before");
        Runtime.getRuntime().exec("docker kill redis-master");
        Thread.sleep(30000);
        repository.save("key_after", "value_after");
        assertEquals("value_after", repository.get("key_after"));
        Runtime.getRuntime().exec("docker start redis-master");
        Thread.sleep(10000);
        repository.save("key_final", "value_final");
        assertEquals("value_final", repository.get("key_final"));
        assertEquals("value_after", repository.get("key_after"));
    }
}