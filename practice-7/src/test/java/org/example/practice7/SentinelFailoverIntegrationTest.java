package org.example.practice7;

import org.example.practice7.repository.SentinelTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SentinelFailoverIntegrationTest {
    @Autowired
    private SentinelTestRepository repository;

    @Test
    void shouldContinueWorkingAfterMasterFailover() throws IOException, InterruptedException {
        repository.save("failover-key", "failover-value");
        assertThat(repository.get("failover-key")).isEqualTo("failover-value");

        Runtime.getRuntime().exec("docker stop redis-master").waitFor();
        System.out.println("redis-master stopped, waiting for sentinel failover...");

        Thread.sleep(20_000);

        repository.save("post-failover-key", "post-failover-value");
        assertThat(repository.get("post-failover-key")).isEqualTo("post-failover-value");

        assertThat(repository.get("failover-key")).isEqualTo("failover-value");
    }
}