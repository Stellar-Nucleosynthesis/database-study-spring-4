package org.example.practice5;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class AbstractPostgresTest {

    private static final String PRIMARY_URL  = "jdbc:postgresql://localhost:5432/testdb";
    private static final String REPLICA_URL  = "jdbc:postgresql://localhost:5433/testdb";

    private static final String USERNAME = "postgres";
    private static final String PRIMARY_PASSWORD = "masterpass";

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.primary.url", () -> PRIMARY_URL);
        registry.add("spring.datasource.primary.username", () -> USERNAME);
        registry.add("spring.datasource.primary.password", () -> PRIMARY_PASSWORD);
        registry.add(
                "spring.datasource.primary.driver-class-name",
                () -> "org.postgresql.Driver"
        );

        registry.add("spring.datasource.replica.url", () -> REPLICA_URL);
        registry.add("spring.datasource.replica.username", () -> USERNAME);
        registry.add("spring.datasource.replica.password", () -> PRIMARY_PASSWORD);
        registry.add(
                "spring.datasource.replica.driver-class-name",
                () -> "org.postgresql.Driver"
        );
    }
}