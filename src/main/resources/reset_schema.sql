CREATE SCHEMA account;

CREATE TABLE account.category
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(45)        NOT NULL UNIQUE
);

CREATE TABLE account.vendor
(
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name        VARCHAR(45)        NOT NULL UNIQUE,
    category_id INT,
    CONSTRAINT category_id
        FOREIGN KEY (category_id) REFERENCES category
);

CREATE TABLE account.transaction
(
    id        UUID      NOT NULL PRIMARY KEY,
    amount    LONG      NOT NULL,
    date      DATE      NOT NULL DEFAULT NOW(),
    vendor_id INT       NOT NULL,

    CONSTRAINT vendor_id
        FOREIGN KEY (vendor_id) REFERENCES vendor
);

INSERT INTO account.category (NAME) VALUES ('식비');
INSERT INTO account.category (NAME) VALUES ('주거');
INSERT INTO account.category (NAME) VALUES ('통신');
INSERT INTO account.category (NAME) VALUES ('교통');
INSERT INTO account.category (NAME) VALUES ('외식');
INSERT INTO account.category (NAME) VALUES ('취미');
INSERT INTO account.category (NAME) VALUES ('쇼핑');
INSERT INTO account.category (NAME) VALUES ('건강');
INSERT INTO account.category (NAME) VALUES ('기타');