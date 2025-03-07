-- V1__create_inventory_table.sql
CREATE TABLE inventory (
    sku VARCHAR(255) PRIMARY KEY,
    quantity INT NOT NULL CHECK (quantity >= 0)
);