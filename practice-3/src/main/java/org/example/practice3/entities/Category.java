package org.example.practice3.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
@NamedQuery(name = "Category.findHavingProductNumGreaterThan",
        query = "SELECT c FROM Category c JOIN c.products p GROUP BY c.id HAVING COUNT(p) > :number")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();
}