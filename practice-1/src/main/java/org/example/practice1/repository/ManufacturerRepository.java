package org.example.practice1.repository;

import org.example.practice1.entities.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerRepository {
    List<Manufacturer> findAll();
    Optional<Manufacturer> findById(Long id);
    Manufacturer save(Manufacturer manufacturer);
}
