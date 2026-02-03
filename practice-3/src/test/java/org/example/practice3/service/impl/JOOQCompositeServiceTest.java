package org.example.practice3.service.impl;

import org.example.practice3.AbstractCompositeServiceTest;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

class JOOQCompositeServiceTest extends AbstractCompositeServiceTest {
    @Autowired
    private JOOQCompositeService jooqCompositeService;

    @Override
    protected ProductService productService() {
        return jooqCompositeService;
    }

    @Override
    protected CategoryService categoryService() {
        return jooqCompositeService;
    }
}