-- drop everything that was previously stored
DROP SCHEMA IF EXISTS account CASCADE;

-- create new schema and tables
CREATE SCHEMA account;

CREATE TABLE account.user
(
    id      INT AUTO_INCREMENT    NOT NULL PRIMARY KEY,
    balance INT         DEFAULT 0 NOT NULL,
    name    VARCHAR(45) DEFAULT 'NO NAME'
);

CREATE TABLE account.category
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(45)        NOT NULL UNIQUE
);

CREATE TABLE account.vendor
(
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name        VARCHAR(45)        NOT NULL UNIQUE,
    category_id INT                NOT NULL,
    CONSTRAINT category_id
        FOREIGN KEY (category_id) REFERENCES category
);

CREATE TABLE account.transaction
(
    id        IDENTITY  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    amount    INT       NOT NULL,
    time      TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id   INT       NOT NULL,
    vendor_id INT       NOT NULL,
    CONSTRAINT user_id
        FOREIGN KEY (user_id) REFERENCES user,
    CONSTRAINT vendor_id
        FOREIGN KEY (vendor_id) REFERENCES vendor
);
