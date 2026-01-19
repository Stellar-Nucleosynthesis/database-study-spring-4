package org.example.practice1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql(scripts = "/db/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SchemaExistenceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void verifyTablesExist() {
        String[] expectedTables = {"manufacturer", "vehicle", "car", "lorry"};
        for (String table : expectedTables) {
            assertTrue(tableExists(table));
        }
    }

    private boolean tableExists(String tableName) {
        try {
            Integer cnt = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.tables " +
                            "WHERE lower(table_name) = ? AND table_schema NOT IN ('information_schema','pg_catalog')",
                    Integer.class, tableName.toLowerCase());
            if (cnt != null && cnt > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
