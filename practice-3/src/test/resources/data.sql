DELETE FROM product;
DELETE FROM category;

ALTER TABLE category ALTER COLUMN id RESTART WITH 1;
ALTER TABLE product ALTER COLUMN id RESTART WITH 1;

INSERT INTO category (name)
VALUES ('Books'), ('Electronics');

INSERT INTO product (name, price, category_id)
VALUES
    ('Book A', 10.00, 1),
    ('Book B', 20.00, 1),
    ('Laptop', 1500.00, 2),
    ('Phone', 800.00, 2);
