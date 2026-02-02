package org.example.practice3.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NamedQuery(name = "Product.findByCategoryName",
        query = "SELECT p FROM Product p JOIN p.category c WHERE c.name = :name")
@NamedQuery(name = "Product.updateNameById",
        query = "UPDATE Product p SET p.name = :name WHERE p.id = :id")
@NamedQuery(name = "Product.deleteByPriceGreaterThan",
        query = "DELETE FROM Product p WHERE p.price > :price")
@NamedQuery(name = "Product.countByCategoryId",
        query = "SELECT COUNT(p) FROM Product p WHERE p.category.id = :id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}