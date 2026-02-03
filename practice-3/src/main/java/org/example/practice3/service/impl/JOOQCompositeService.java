package org.example.practice3.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.practice3.entities.Category;
import org.example.practice3.entities.Product;
import org.example.practice3.service.CategoryService;
import org.example.practice3.service.ProductService;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.jooq.impl.DSL.count;

@Service
@RequiredArgsConstructor
public class JOOQCompositeService implements ProductService, CategoryService {
    private final DSLContext dsl;

    @Override
    public List<Category> findCategoriesHavingProductNumGreaterThan(Integer number) {
        var C = DSL.table(DSL.name("CATEGORY")).as(DSL.name("C"));
        var P = DSL.table(DSL.name("PRODUCT")).as(DSL.name("P"));

        var C_ID   = DSL.field(DSL.name("C", "ID"), Long.class);
        var C_NAME = DSL.field(DSL.name("C", "NAME"), String.class);
        var P_ID   = DSL.field(DSL.name("P", "ID"), Long.class);
        var P_CAT  = DSL.field(DSL.name("P", "CATEGORY_ID"), Long.class);

        var result = dsl
                .select(C_ID, C_NAME)
                .from(C)
                .join(P).on(P_CAT.eq(C_ID))
                .groupBy(C_ID, C_NAME)
                .having(count(P_ID).gt(number))
                .fetch();

        return result.map(r -> {
            Category cat = new Category();
            cat.setId(r.get(C_ID));
            cat.setName(r.get(C_NAME));
            return cat;
        });
    }

    @Override
    public List<Product> findProductsByCategoryName(String name) {
        var P = DSL.table(DSL.name("PRODUCT")).as(DSL.name("P"));
        var C = DSL.table(DSL.name("CATEGORY")).as(DSL.name("C"));

        var P_ID    = DSL.field(DSL.name("P", "ID"), Long.class);
        var P_NAME  = DSL.field(DSL.name("P", "NAME"), String.class);
        var P_PRICE = DSL.field(DSL.name("P", "PRICE"), BigDecimal.class);
        var P_CAT   = DSL.field(DSL.name("P", "CATEGORY_ID"), Long.class);
        var C_ID    = DSL.field(DSL.name("C", "ID"), Long.class);
        var C_NAME  = DSL.field(DSL.name("C", "NAME"), String.class);

        var result = dsl
                .select(P_ID, P_NAME, P_PRICE, C_ID, C_NAME)
                .from(P)
                .join(C).on(P_CAT.eq(C_ID))
                .where(C_NAME.eq(name))
                .fetch();

        return result.map(r -> {
            Category cat = new Category();
            cat.setId(r.get(C_ID));
            cat.setName(r.get(C_NAME));

            Product prod = new Product();
            prod.setId(r.get(P_ID));
            prod.setName(r.get(P_NAME));
            prod.setPrice(r.get(P_PRICE));
            prod.setCategory(cat);

            return prod;
        });
    }

    @Override
    public void updateProductNameById(Long id, String name) {
        var PRODUCT = DSL.table(DSL.name("PRODUCT"));
        var PROD_NAME = DSL.field(DSL.name("PRODUCT", "NAME"), String.class);
        var PROD_ID   = DSL.field(DSL.name("PRODUCT", "ID"), Long.class);

        dsl.update(PRODUCT)
                .set(PROD_NAME, name)
                .where(PROD_ID.eq(id))
                .execute();
    }

    @Override
    public void deleteProductsByPriceGreaterThan(BigDecimal price) {
        var PRODUCT = DSL.table(DSL.name("PRODUCT"));
        var PROD_PRICE = DSL.field(DSL.name("PRODUCT", "PRICE"), BigDecimal.class);

        dsl.delete(PRODUCT)
                .where(PROD_PRICE.gt(price))
                .execute();
    }

    @Override
    public List<Product> findProductsByCategoryNameAndPriceGreaterThan(String name, BigDecimal price) {
        var P = DSL.table(DSL.name("PRODUCT")).as(DSL.name("P"));
        var C = DSL.table(DSL.name("CATEGORY")).as(DSL.name("C"));

        var P_ID    = DSL.field(DSL.name("P", "ID"), Long.class);
        var P_NAME  = DSL.field(DSL.name("P", "NAME"), String.class);
        var P_PRICE = DSL.field(DSL.name("P", "PRICE"), BigDecimal.class);
        var P_CAT   = DSL.field(DSL.name("P", "CATEGORY_ID"), Long.class);
        var C_ID    = DSL.field(DSL.name("C", "ID"), Long.class);
        var C_NAME  = DSL.field(DSL.name("C", "NAME"), String.class);

        Condition condition = DSL.noCondition();
        if (name != null)  condition = condition.and(C_NAME.eq(name));
        if (price != null) condition = condition.and(P_PRICE.gt(price));

        var result = dsl
                .select(P_ID, P_NAME, P_PRICE, C_ID, C_NAME)
                .from(P)
                .join(C).on(P_CAT.eq(C_ID))
                .where(condition)
                .fetch();

        return result.map(r -> {
            Category cat = new Category();
            cat.setId(r.get(C_ID));
            cat.setName(r.get(C_NAME));

            Product prod = new Product();
            prod.setId(r.get(P_ID));
            prod.setName(r.get(P_NAME));
            prod.setPrice(r.get(P_PRICE));
            prod.setCategory(cat);

            return prod;
        });
    }

    @Override
    public long countProductsByCategoryId(Long id) {
        var PRODUCT = DSL.table(DSL.name("PRODUCT"));
        var PROD_CAT = DSL.field(DSL.name("PRODUCT", "CATEGORY_ID"), Long.class);

        return dsl.fetchCount(PRODUCT, PROD_CAT.eq(id));
    }
}
