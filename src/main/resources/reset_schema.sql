-- drop everything that was previously stored
DROP SCHEMA IF EXISTS accounts CASCADE;

-- create new schema and tables
CREATE SCHEMA accounts;

CREATE TABLE accounts.user
(
    id      INT AUTO_INCREMENT    NOT NULL PRIMARY KEY,
    balance INT         DEFAULT 0 NOT NULL,
    name    VARCHAR(45) DEFAULT 'NO NAME'
);

CREATE TABLE accounts.category
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(45)        NOT NULL UNIQUE
);

CREATE TABLE accounts.vendor
(
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name        VARCHAR(45)        NOT NULL UNIQUE,
    category_id INT                NOT NULL,
    CONSTRAINT category_id
        FOREIGN KEY (category_id) REFERENCES category
);

CREATE TABLE accounts.transaction
(
    id        IDENTITY  NOT NULL PRIMARY KEY,
    amount    INT       NOT NULL,
    time      TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id   INT       NOT NULL,
    vendor_id INT       NOT NULL,
    CONSTRAINT user_id
        FOREIGN KEY (user_id) REFERENCES user,
    CONSTRAINT vendor_id
        FOREIGN KEY (vendor_id) REFERENCES vendor
);
