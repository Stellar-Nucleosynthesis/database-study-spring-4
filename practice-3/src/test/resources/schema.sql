CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price       DECIMAL(10, 2),
    category_id BIGINT,
    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES category (id)
    );
