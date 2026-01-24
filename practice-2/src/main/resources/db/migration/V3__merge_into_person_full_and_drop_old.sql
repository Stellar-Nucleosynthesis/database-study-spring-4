CREATE TABLE person_full
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    birth_date     DATE,
    salary         DECIMAL(19, 2),
    full_name      VARCHAR(255),
    salary_rounded BIGINT
);

INSERT INTO person_full(id, first_name, last_name, birth_date, salary, full_name, salary_rounded)
SELECT p.id, p.first_name, p.last_name, p.birth_date, p.salary, pp.full_name, pp.salary_rounded
FROM person p
         LEFT JOIN person_profile pp ON pp.person_id = p.id;

DROP TABLE person_profile;
DROP TABLE person;