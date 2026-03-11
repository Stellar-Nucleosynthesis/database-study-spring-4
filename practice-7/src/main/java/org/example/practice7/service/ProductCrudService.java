package org.example.practice7.service;

import lombok.RequiredArgsConstructor;
import org.example.practice7.entity.Product;
import org.example.practice7.repository.ProductCrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCrudService {
    private final ProductCrudRepository repository;

    public void save(Product product) {
        repository.save(product);
    }

    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Iterable<Product> findAll() {
        return repository.findAll();
    }
}