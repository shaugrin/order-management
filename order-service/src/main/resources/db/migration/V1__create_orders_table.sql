CREATE TABLE orders (
    id VARCHAR(36) PRIMARY KEY,
    sku VARCHAR(36) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(20) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE orders
    ADD COLUMN customer_email VARCHAR(255) NOT NULL,
    ADD COLUMN customer_phone VARCHAR(20) NOT NULL;