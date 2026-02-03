package org.example.practice3;

import jakarta.persistence.EntityManager;
import org.example.practice3.entities.Category;
import org.example.practice3.entities.Product;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractCompositeServiceTest {
    @Autowired
    private EntityManager em;

    protected abstract ProductService productService();
    protected abstract CategoryService categoryService();

    @Test
    void findProductsByCategoryName() {
        List<Product> products = productService().findProductsByCategoryName("Books");

        assertEquals(2, products.size());
        assertTrue(products.stream().allMatch(p -> p.getCategory().getName().equals("Books")));
    }

    @Test
    void findProductsByCategoryNameAndPriceGreaterThan() {
        List<Product> products = productService()
                .findProductsByCategoryNameAndPriceGreaterThan("Books", new BigDecimal("15"));

        assertEquals(1, products.size());
        assertEquals("Book B", products.getFirst().getName());
    }

    @Test
    void updateProductNameById() {
        Product product = productService().findProductsByCategoryName("Books").getFirst();
        Long id = product.getId();

        productService().updateProductNameById(id, "Updated Book");
        em.flush();
        em.clear();

        Product updated =
                productService().findProductsByCategoryName("Books")
                        .stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElseThrow();

        assertEquals("Updated Book", updated.getName());
    }

    @Test
    void deleteProductsByPriceGreaterThan() {
        productService().deleteProductsByPriceGreaterThan(new BigDecimal("1000"));

        List<Product> electronics = productService().findProductsByCategoryName("Electronics");
        assertEquals(1, electronics.size());
        assertEquals("Phone", electronics.getFirst().getName());
    }

    @Test
    void countProductsByCategoryId() {
        long count = productService().countProductsByCategoryId(1L);
        assertEquals(2, count);
    }

    @Test
    void findCategoriesHavingProductNumGreaterThan() {
        List<Category> categories =
                categoryService().findCategoriesHavingProductNumGreaterThan(1);

        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Books")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Electronics")));
    }
}
