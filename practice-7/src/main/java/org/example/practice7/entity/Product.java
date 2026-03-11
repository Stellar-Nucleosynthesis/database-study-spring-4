package org.example.practice7.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@RedisHash("Product")
public class Product {
    private String id;
    private String name;
    private String category;
    private String price;
}