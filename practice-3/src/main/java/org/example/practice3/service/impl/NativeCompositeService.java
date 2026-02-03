package org.example.practice3.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.example.practice3.entities.Category;
import org.example.practice3.entities.Product;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NativeCompositeService implements ProductService, CategoryService {
    private final EntityManager em;

    @Override
    public List<Category> findCategoriesHavingProductNumGreaterThan(Integer number) {
        String sql = """
            SELECT c.*
            FROM category c
            JOIN product p ON p.category_id = c.id
            GROUP BY c.id
            HAVING COUNT(p.id) > :number
        """;

        return em.createNativeQuery(sql, Category.class)
                .setParameter("number", number)
                .getResultList();
    }

    @Override
    public List<Product> findProductsByCategoryName(String name) {
        String sql = """
            SELECT p.*
            FROM product p
            JOIN category c ON p.category_id = c.id
            WHERE c.name = :name
        """;

        return em.createNativeQuery(sql, Product.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void updateProductNameById(Long id, String name) {
        String sql = """
            UPDATE product
            SET name = :name
            WHERE id = :id
        """;

        em.createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public void deleteProductsByPriceGreaterThan(BigDecimal price) {
        String sql = """
            DELETE FROM product
            WHERE price > :price
        """;

        em.createNativeQuery(sql)
                .setParameter("price", price)
                .executeUpdate();
    }

    @Override
    public List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.*
            FROM product p
            JOIN category c ON p.category_id = c.id
            WHERE 1=1
        """);

        if (name != null) {
            sql.append(" AND c.name = :name");
        }

        if (price != null) {
            sql.append(" AND p.price > :price");
        }

        Query query = em.createNativeQuery(sql.toString(), Product.class);

        if (name != null) {
            query.setParameter("name", name);
        }

        if (price != null) {
            query.setParameter("price", price);
        }

        return query.getResultList();
    }

    @Override
    public long countProductsByCategoryId(Long id) {
        String sql = """
            SELECT COUNT(*)
            FROM product
            WHERE category_id = :id
        """;

        Number result = (Number) em.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        return result.intValue();
    }
}
