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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class DatabaseMaxConnectionsTest extends AbstractPostgresTest {
    @Autowired
    private DataSource dataSource;

    @DynamicPropertySource
    static void registerHikariProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.primary.maximum-pool-size", () -> 20);
        registry.add("spring.datasource.primary.connection-timeout", () -> 1000);

        registry.add("spring.datasource.replica.maximum-pool-size", () -> 20);
        registry.add("spring.datasource.replica.connection-timeout", () -> 1000);
    }

    @Test
    void whenDatabaseConnectionLimitExceeded_thenApplicationFails() throws Exception {
        try(HikariDataSource hikari = dataSource.unwrap(HikariDataSource.class)){
            assertThatThrownBy(() -> {
                List<Connection> connections = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    connections.add(hikari.getConnection());
                }
            })
                    .isInstanceOf(SQLException.class);
        }
    }
}