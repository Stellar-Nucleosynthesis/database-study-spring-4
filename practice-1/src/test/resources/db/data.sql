TRUNCATE TABLE manufacturer RESTART IDENTITY CASCADE;
INSERT INTO manufacturer (name, description) VALUES ('ACME', 'Industrial');
INSERT INTO manufacturer (name, description) VALUES ('Maker', 'Gadgets');
