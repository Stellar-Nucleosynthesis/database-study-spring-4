package org.example.practice3.service;

import org.example.practice3.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoriesHavingProductNumGreaterThan(Integer number);
}
