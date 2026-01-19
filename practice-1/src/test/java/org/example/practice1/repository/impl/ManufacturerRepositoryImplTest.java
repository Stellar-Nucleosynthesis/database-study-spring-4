package org.example.practice1.repository.impl;

import org.example.practice1.entities.Manufacturer;
import org.example.practice1.utils.AbstractTestContainersTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/db/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/db/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ManufacturerRepositoryImplTest extends AbstractTestContainersTest {

    @Autowired
    private ManufacturerRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findById_shouldReturnOptionalWithEntity_whenExists() {
        Manufacturer m = new Manufacturer();
        m.setName("Maker");
        m.setDescription("Makes stuff");
        Manufacturer saved = repository.save(m);

        Optional<Manufacturer> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        Manufacturer foundEntity = found.get();
        assertEquals(saved.getId(), foundEntity.getId());
        assertEquals("Maker", foundEntity.getName());
        assertEquals("Makes stuff", foundEntity.getDescription());
    }

    @Test
    void findAll_shouldReturnAllPersistedManufacturers() {
        Manufacturer a = new Manufacturer();
        a.setName("A");
        a.setDescription("Desc A");
        Manufacturer b = new Manufacturer();
        b.setName("B");
        b.setDescription("Desc B");

        repository.save(a);
        repository.save(b);

        List<Manufacturer> all = repository.findAll();
        assertNotNull(all);
        assertEquals(4, all.size());

        assertTrue(all.stream().anyMatch(m -> "A".equals(m.getName())
                && "Desc A".equals(m.getDescription())));
        assertTrue(all.stream().anyMatch(m -> "B".equals(m.getName())
                && "Desc B".equals(m.getDescription())));
    }

    @Test
    void save_shouldInsertAndReturnEntityWithId() {
        Manufacturer m = new Manufacturer();
        m.setName("ACME Corp");
        m.setDescription("Industrial goods");

        Manufacturer saved = repository.save(m);

        assertNotNull(saved, "Saved must not be null");
        assertNotNull(saved.getId(), "Generated id must not be null");

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM manufacturer WHERE id = ? AND name = ? AND description = ?",
                Long.class,
                saved.getId(), "ACME Corp", "Industrial goods");
        assertEquals(1L, count);
    }

    @Test
    void save_withExistingId_shouldUpdateRow() {
        Manufacturer m = new Manufacturer();
        m.setName("Initial");
        m.setDescription("InitialDesc");
        Manufacturer saved = repository.save(m);

        saved.setName("Updated");
        saved.setDescription("UpdatedDesc");
        Manufacturer updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        String name = jdbcTemplate.queryForObject(
                "SELECT name FROM manufacturer WHERE id = ?",
                String.class,
                saved.getId()
        );
        assertEquals("Updated", name);
    }
}