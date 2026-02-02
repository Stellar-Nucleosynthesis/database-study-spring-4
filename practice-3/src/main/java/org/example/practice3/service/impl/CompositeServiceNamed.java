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
public class CompositeServiceNamed implements ProductService, CategoryService {
    private final EntityManager em;

    @Override
    public List<Category> findCategoriesHavingProductNumGreaterThan(Integer number) {
        TypedQuery<Category> query =
                em.createNamedQuery("Category.findHavingProductNumGreaterThan", Category.class);
        return query.setParameter("number", number)
                .getResultList();
    }

    @Override
    public List<Product> findProductsByCategoryName(String name) {
        TypedQuery<Product> query =
                em.createNamedQuery("Product.findByCategoryName", Product.class);
        return query.setParameter("name", name)
                .getResultList();
    }

    @Override
    public void updateProductNameById(Long id, String name) {
        em.createNamedQuery("Product.updateNameById")
                .setParameter("id", id)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public void deleteProductsByPriceGreaterThan(BigDecimal price) {
        em.createNamedQuery("Product.deleteByPriceGreaterThan")
                .setParameter("price", price)
                .executeUpdate();
    }

    @Override
    public List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int countProductsByCategoryId(Long id) {
        return em.createNamedQuery("Product.countByCategoryId", Integer.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
