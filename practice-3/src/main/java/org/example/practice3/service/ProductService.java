package org.example.practice3.service;

import org.example.practice3.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> findProductsByCategoryName(String name);
    void updateProductNameById(Long id, String name);
    void deleteProductsByPriceGreaterThan(BigDecimal price);
    List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price);
    int countProductsByCategoryId(Long id);
}
