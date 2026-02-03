package org.example.practice3.service.impl;

import org.example.practice3.AbstractCompositeServiceTest;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;


class JPQLCompositeServiceTest extends AbstractCompositeServiceTest {
    @Autowired
    private JPQLCompositeService jpqlCompositeService;

    @Override
    protected ProductService productService() {
        return jpqlCompositeService;
    }

    @Override
    protected CategoryService categoryService() {
        return jpqlCompositeService;
    }
}