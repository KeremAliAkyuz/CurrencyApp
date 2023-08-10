CREATE TABLE IF NOT EXISTS currencies (
    id INT NOT NULL PRIMARY KEY,
    bank VARCHAR(255),
    currency VARCHAR(255),
    buy FLOAT,
    sell FLOAT,
    date TIMESTAMP,
    deleted BOOLEAN
);

