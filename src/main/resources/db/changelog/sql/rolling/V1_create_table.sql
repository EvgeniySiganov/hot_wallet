CREATE TABLE wallet
(
    guid    UUID PRIMARY KEY NOT NULL,
    balance NUMERIC(20, 2)   NOT NULL
);