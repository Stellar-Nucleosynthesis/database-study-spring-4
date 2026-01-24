CREATE TABLE person
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    birth_date DATE,
    salary     DECIMAL(19, 2)
);

INSERT INTO person(first_name, last_name, birth_date, salary)
VALUES ('Іван', 'Іваненко', DATE '1985-03-15', 1234.56),
       ('Олена', 'Петренко', DATE '1990-07-22', 2345.00),
       ('Павло', 'Сидоренко', DATE '1978-11-02', 3100.75);
