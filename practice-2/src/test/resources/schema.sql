DROP TABLE IF EXISTS person_full;

CREATE TABLE person_full
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    birth_date     DATE,
    salary         DECIMAL(19, 2),
    full_name      VARCHAR(255),
    salary_rounded BIGINT
);
