CREATE TABLE countries (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(3) NOT NULL,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_countries_code UNIQUE (code),
    CONSTRAINT uk_countries_name UNIQUE (name)
);

INSERT INTO countries (code, name, active) VALUES
    ('IN', 'India', TRUE),
    ('NP', 'Nepal', TRUE),
    ('PH', 'Philippines', TRUE),
    ('US', 'USA', TRUE);
