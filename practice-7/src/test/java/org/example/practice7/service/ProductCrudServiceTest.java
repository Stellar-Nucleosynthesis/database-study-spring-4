package org.example.practice7.service;

import org.example.practice7.entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCrudServiceTest {
    static RedisServer redisServer;

    @Autowired
    ProductCrudService service;

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
    void save_and_find_product() {
        Product p = new Product("1", "Laptop", "Electronics", "1000");
        service.save(p);
        Optional<Product> loaded = service.findById("1");
        assertTrue(loaded.isPresent());
        assertEquals("Laptop", loaded.get().getName());
        assertEquals("1000", loaded.get().getPrice());
    }

    @Test
    void delete_product() {
        Product p = new Product("2", "Phone", "Electronics", "800");
        service.save(p);
        service.delete("2");
        Optional<Product> loaded = service.findById("2");
        assertTrue(loaded.isEmpty());
    }

    @Test
    void find_all_products() {
        Product p1 = new Product("3", "Tablet", "Electronics", "500");
        Product p2 = new Product("4", "Monitor", "Hardware", "300");
        service.save(p1);
        service.save(p2);
        Iterable<Product> allProducts = service.findAll();
        int count = 0;
        for (Product p : allProducts) count++;
        assertEquals(2, count);
    }
}