CREATE TABLE person_profile
(
    person_id      BIGINT PRIMARY KEY,
    full_name      VARCHAR(255) NOT NULL,
    salary_rounded BIGINT
);

INSERT INTO person_profile(person_id, full_name, salary_rounded)
SELECT id, (first_name || ' ' || last_name) AS full_name, CAST(ROUND(salary) AS BIGINT)
FROM person;