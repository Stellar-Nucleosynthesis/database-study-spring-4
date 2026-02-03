package org.example.practice3.service.impl;

import org.example.practice3.AbstractCompositeServiceTest;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

class NativeCompositeServiceTest extends AbstractCompositeServiceTest {
    @Autowired
    private NativeCompositeService nativeCompositeService;

    @Override
    protected ProductService productService() {
        return nativeCompositeService;
    }

    @Override
    protected CategoryService categoryService() {
        return nativeCompositeService;
    }
}