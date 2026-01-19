package org.example.practice1.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.practice1.entities.Manufacturer;
import org.example.practice1.repository.ManufacturerRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManufacturerRepositoryImpl implements ManufacturerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Manufacturer> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM manufacturer",
                Manufacturer.getRowMapper());
    }

    @Override
    public Optional<Manufacturer> findById(Long id) {
        Objects.requireNonNull(id, "Id must not be null");
        try {
            Manufacturer result = jdbcTemplate.queryForObject(
                    "SELECT * FROM manufacturer WHERE id = ?",
                    Manufacturer.getRowMapper(), id);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Manufacturer save(Manufacturer manufacturer) {
        Objects.requireNonNull(manufacturer, "Manufacturer must not be null");
        if (manufacturer.getName() == null || manufacturer.getName().isBlank()) {
            throw new IllegalArgumentException("Manufacturer.name must not be null or blank");
        }
        if (manufacturer.getDescription() == null) {
            throw new IllegalArgumentException("Manufacturer.description must not be null");
        }

        if (manufacturer.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO manufacturer (name, description) VALUES (?, ?)",
                        new String[] { "id" }
                );
                ps.setString(1, manufacturer.getName());
                ps.setString(2, manufacturer.getDescription());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new IllegalStateException("Failed to retrieve generated id for Manufacturer");
            }
            manufacturer.setId(key.longValue());
            return manufacturer;
        } else {
            int updated = jdbcTemplate.update(
                    "UPDATE manufacturer SET name = ?, description = ? WHERE id = ?",
                    manufacturer.getName(),
                    manufacturer.getDescription(),
                    manufacturer.getId()
            );

            if (updated == 0) {
                throw new IllegalStateException("Manufacturer with id=" + manufacturer.getId() + " not found for update");
            }

            return manufacturer;
        }
    }
}
