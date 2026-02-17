package org.example.practice5;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractPostgresTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);

    @DynamicPropertySource
    static void overrideDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.primary.url",          POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.primary.username",     POSTGRES::getUsername);
        registry.add("spring.datasource.primary.password",     POSTGRES::getPassword);
        registry.add("spring.datasource.primary.driver-class-name",
                () -> "org.postgresql.Driver");

        registry.add("spring.datasource.replica.url",          POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.replica.username",     POSTGRES::getUsername);
        registry.add("spring.datasource.replica.password",     POSTGRES::getPassword);
        registry.add("spring.datasource.replica.driver-class-name",
                () -> "org.postgresql.Driver");
    }
}