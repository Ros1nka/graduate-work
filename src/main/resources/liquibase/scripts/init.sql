--liquibase formatted sql

--changeset Ros1nka:1
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    image VARCHAR(255),
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE ads(
    pk SERIAL PRIMARY KEY,
    title VARCHAR(32) NOT NULL,
    description VARCHAR(64),
    price INTEGER NOT NULL,
    image VARCHAR(255),
    author_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE comments(
    pk SERIAL PRIMARY KEY,
    text VARCHAR(64) NOT NULL,
    author_id INTEGER NOT NULL,
    ad_pk INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (ad_pk) REFERENCES ads (pk) ON DELETE CASCADE
);