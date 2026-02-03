package org.example.practice3.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
public class JPQLCompositeService implements ProductService, CategoryService {
    private final EntityManager em;

    @Override
    public List<Category> findCategoriesHavingProductNumGreaterThan(Integer number) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c JOIN c.products p " +
                        "GROUP BY c.id HAVING COUNT(p) > :number", Category.class);
        return query.setParameter("number", number)
                .getResultList();
    }

    @Override
    public List<Product> findProductsByCategoryName(String name) {
        TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product p JOIN p.category c WHERE c.name = :name",
                Product.class);
        return query.setParameter("name", name)
                .getResultList();
    }

    @Override
    public void updateProductNameById(Long id, String name) {
        em.createQuery("UPDATE Product p SET p.name = :name WHERE p.id = :id")
                .setParameter("id", id)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public void deleteProductsByPriceGreaterThan(BigDecimal price) {
        em.createQuery("DELETE FROM Product p WHERE p.price > :price")
                .setParameter("price", price)
                .executeUpdate();
    }

    @Override
    public List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Product p JOIN p.category c WHERE 1=1");
        if (name != null)
            jpql.append(" AND c.name = :name");
        if (price != null)
            jpql.append(" AND p.price > :price");

        TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);

        if (name != null)
            query.setParameter("name", name);
        if (price != null)
            query.setParameter("price", price);

        return query.getResultList();
    }

    @Override
    public long countProductsByCategoryId(Long id) {
        Long result = em.createQuery(
                        "SELECT COUNT(p) FROM Product p WHERE p.category.id = :id",
                        Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return result.intValue();
    }
}
