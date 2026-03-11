package org.example.practice7.repository;

import org.example.practice7.entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTemplateRepositoryTest {
    static RedisServer redisServer;

    @Autowired
    ProductTemplateRepository repository;

    @BeforeAll
    static void startRedis() {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    static void stopRedis() {
        redisServer.stop();
    }

    @Test
    void save_and_findById() {
        Product p = new Product("1", "Laptop", "Electronics", "1000");
        repository.save(p);
        Optional<Product> loaded = repository.findById("1");
        assertTrue(loaded.isPresent());
        assertEquals("Laptop", loaded.get().getName());
        assertEquals("Electronics", loaded.get().getCategory());
        assertEquals("1000", loaded.get().getPrice());
    }

    @Test
    void findById_nonexistent_returns_empty() {
        Optional<Product> loaded = repository.findById("999");
        assertTrue(loaded.isEmpty());
    }

    @Test
    void delete_product() {
        Product p = new Product("2", "Phone", "Electronics", "800");
        repository.save(p);
        repository.delete("2");
        assertTrue(repository.findById("2").isEmpty());
    }

    @Test
    void addToCategoryList_and_getCategoryList() {
        Product p1 = new Product("3", "Tablet", "Electronics", "500");
        Product p2 = new Product("4", "Monitor", "Electronics", "300");
        repository.addToCategoryList("Electronics", p1);
        repository.addToCategoryList("Electronics", p2);
        List<Product> products = repository.getCategoryList("Electronics");
        assertFalse(products.isEmpty());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Tablet")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Monitor")));
    }

    @Test
    void getCategoryList_preserves_insertion_order() {
        Product first = new Product("5", "Keyboard", "Peripherals", "100");
        Product second = new Product("6", "Mouse", "Peripherals", "50");
        repository.addToCategoryList("Peripherals", first);
        repository.addToCategoryList("Peripherals", second);
        List<Product> products = repository.getCategoryList("Peripherals");
        assertEquals("Keyboard", products.get(0).getName());
        assertEquals("Mouse", products.get(1).getName());
    }

    @Test
    void addProductNameToSet_and_isProductNameExist() {
        repository.addProductNameToSet("Laptop");
        assertTrue(repository.isProductNameExist("Laptop"));
    }

    @Test
    void isProductNameExist_returns_false_for_missing_name() {
        assertFalse(repository.isProductNameExist("NonExistentProduct"));
    }
}