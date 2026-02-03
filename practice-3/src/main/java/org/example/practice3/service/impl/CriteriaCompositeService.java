package org.example.practice3.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.practice3.entities.Category;
import org.example.practice3.entities.Product;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CriteriaCompositeService implements ProductService, CategoryService {
    private final EntityManager em;

    @Override
    public List<Category> findCategoriesHavingProductNumGreaterThan(Integer number) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);

        Root<Category> category = cq.from(Category.class);
        Join<Category, Product> products = category.join("products");

        cq.select(category)
                .groupBy(category.get("id"))
                .having(cb.gt(cb.count(products), number));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Product> findProductsByCategoryName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> product = cq.from(Product.class);
        Join<Product, Category> category = product.join("category");

        cq.select(product)
                .where(cb.equal(category.get("name"), name));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public void updateProductNameById(Long id, String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Product> update = cb.createCriteriaUpdate(Product.class);

        Root<Product> product = update.from(Product.class);

        update.set(product.get("name"), name)
                .where(cb.equal(product.get("id"), id));

        em.createQuery(update).executeUpdate();
    }

    @Override
    public void deleteProductsByPriceGreaterThan(BigDecimal price) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Product> delete = cb.createCriteriaDelete(Product.class);

        Root<Product> product = delete.from(Product.class);

        delete.where(cb.gt(product.get("price"), price));

        em.createQuery(delete).executeUpdate();
    }

    @Override
    public List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> product = cq.from(Product.class);
        Join<Product, Category> category = product.join("category");

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(cb.equal(category.get("name"), name));
        }

        if (price != null) {
            predicates.add(cb.gt(product.get("price"), price));
        }

        cq.select(product)
                .where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public long countProductsByCategoryId(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Product> product = cq.from(Product.class);

        cq.select(cb.count(product))
                .where(cb.equal(product.get("category").get("id"), id));

        return em.createQuery(cq).getSingleResult().intValue();
    }
}
