package org.example.practice7.repository;

import org.example.practice7.entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
    static RedisServer redisServer;

    @Autowired
    ProductRepository repository;

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
    void create_and_read_product() {
        Product p = new Product("1","Laptop","Electronics","1000");
        repository.save(p);
        Optional<Product> loaded = repository.findById("1");
        assertTrue(loaded.isPresent());
        assertEquals("Laptop", loaded.get().getName());
    }

    @Test
    void update_product() {
        Product p = new Product("2","Phone","Electronics","800");
        repository.save(p);
        p.setPrice("900");
        repository.update(p);
        Optional<Product> updated = repository.findById("2");
        assertTrue(updated.isPresent());
        assertEquals("900", updated.get().getPrice());
    }

    @Test
    void partial_update_single_field() {
        Product p = new Product("3","Tablet","Electronics","500");
        repository.save(p);
        repository.updatePrice("3", "550");
        Optional<Product> updated = repository.findById("3");
        assertTrue(updated.isPresent());
        assertEquals("550", updated.get().getPrice());
    }

    @Test
    void universal_patch_update() {
        Product p = new Product("4","Monitor","Electronics","300");
        repository.save(p);
        repository.patch("4", Map.of(
                "price","350",
                "category","Hardware"
        ));
        Optional<Product> updated = repository.findById("4");
        assertTrue(updated.isPresent());
        assertEquals("350", updated.get().getPrice());
        assertEquals("Hardware", updated.get().getCategory());
    }

    @Test
    void delete_product() {
        Product p = new Product("5","Mouse","Electronics","20");
        repository.save(p);
        repository.delete("5");
        assertTrue(repository.findById("5").isEmpty());
    }
}