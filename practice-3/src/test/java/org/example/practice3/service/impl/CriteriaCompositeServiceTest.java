package org.example.practice3.service.impl;

import org.example.practice3.AbstractCompositeServiceTest;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

class CriteriaCompositeServiceTest extends AbstractCompositeServiceTest {
    @Autowired
    private CriteriaCompositeService criteriaCompositeService;

    @Override
    protected ProductService productService() {
        return criteriaCompositeService;
    }

    @Override
    protected CategoryService categoryService() {
        return criteriaCompositeService;
    }
}