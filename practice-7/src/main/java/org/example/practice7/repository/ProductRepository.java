package org.example.practice7.repository;

import lombok.RequiredArgsConstructor;
import org.example.practice7.entity.Product;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final StringRedisTemplate redisTemplate;

    private String key(String id) {
        return "product:" + id;
    }

    public void save(Product product) {
        Map<String, String> map = new HashMap<>();
        map.put("name", product.getName());
        map.put("category", product.getCategory());
        map.put("price", product.getPrice());
        redisTemplate.opsForHash().putAll(key(product.getId()), map);
    }

    public Optional<Product> findById(String id) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key(id));
        if (map.isEmpty())
            return Optional.empty();
        Product p = new Product(
                id,
                (String) map.get("name"),
                (String) map.get("category"),
                (String) map.get("price")
        );
        return Optional.of(p);
    }

    public void update(Product product) {
        save(product);
    }

    public void delete(String id) {
        redisTemplate.delete(key(id));
    }

    public void updateName(String id, String value) {
        redisTemplate.opsForHash().put(key(id), "name", value);
    }

    public void updateCategory(String id, String value) {
        redisTemplate.opsForHash().put(key(id), "category", value);
    }

    public void updatePrice(String id, String value) {
        redisTemplate.opsForHash().put(key(id), "price", value);
    }

    public void patch(String id, Map<String,String> patch) {
        redisTemplate.opsForHash().putAll(key(id), patch);
    }
}