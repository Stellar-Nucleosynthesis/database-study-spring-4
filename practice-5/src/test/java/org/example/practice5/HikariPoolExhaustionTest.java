package org.example.practice5;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class HikariPoolExhaustionTest extends AbstractPostgresTest {

    @Autowired
    private DataSource dataSource;

    @DynamicPropertySource
    static void registerHikariProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.primary.maximum-pool-size", () -> 1);
        registry.add("spring.datasource.primary.connection-timeout", () -> 1000);

        registry.add("spring.datasource.replica.maximum-pool-size", () -> 1);
        registry.add("spring.datasource.replica.connection-timeout", () -> 1000);
    }

    @Test
    void whenPoolIsExhausted_thenNewRequestFails() throws Exception {
        HikariDataSource hikari = dataSource.unwrap(HikariDataSource.class);
        Connection c1 = hikari.getConnection();

        long start = System.currentTimeMillis();
        assertThatThrownBy(hikari::getConnection)
                .isInstanceOf(SQLException.class)
                .hasMessageContaining("Connection is not available");
        long duration = System.currentTimeMillis() - start;

        assertThat(duration).isGreaterThanOrEqualTo(1000);
        c1.close();
    }
}