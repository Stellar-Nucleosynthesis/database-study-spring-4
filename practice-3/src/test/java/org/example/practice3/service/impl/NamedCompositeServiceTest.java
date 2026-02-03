package org.example.practice3.service.impl;

import org.example.practice3.AbstractCompositeServiceTest;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

class NamedCompositeServiceTest extends AbstractCompositeServiceTest {
    @Autowired
    private NamedCompositeService namedCompositeService;

    @Override
    protected ProductService productService() {
        return namedCompositeService;
    }

    @Override
    protected CategoryService categoryService() {
        return namedCompositeService;
    }
}