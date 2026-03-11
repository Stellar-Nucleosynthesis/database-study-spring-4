package org.example.practice7.repository;

import lombok.RequiredArgsConstructor;
import org.example.practice7.entity.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductTemplateRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "productObj:";

    private String key(String id) {
        return KEY_PREFIX + id;
    }

    public void save(Product product) {
        redisTemplate.opsForValue().set(key(product.getId()), product);
    }

    public Optional<Product> findById(String id) {
        Product product = (Product) redisTemplate.opsForValue().get(key(id));
        return Optional.ofNullable(product);
    }

    public void delete(String id) {
        redisTemplate.delete(key(id));
    }

    public void addToCategoryList(String category, Product product) {
        redisTemplate.opsForList().rightPush("categoryList:" + category, product);
    }

    public List<Product> getCategoryList(String category) {
        return redisTemplate.opsForList().range("categoryList:" + category, 0, -1)
                .stream()
                .map(o -> (Product) o)
                .toList();
    }

    public void addProductNameToSet(String name) {
        redisTemplate.opsForSet().add("productNames", name);
    }

    public boolean isProductNameExist(String name) {
        return redisTemplate.opsForSet().isMember("productNames", name);
    }
}