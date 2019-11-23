CREATE SCHEMA IF NOT EXISTS order_management;

CREATE TABLE IF NOT EXISTS order_management.orders
(
    order_id       SERIAL      NOT NULL PRIMARY KEY,
    user_name       VARCHAR(100) NOT NULL,
    status         VARCHAR(20) NOT NULL,
    total_cost      BIGINT check (total_cost >= 0) NOT NULL,
    total_amount    INTEGER check (total_amount > 0) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_management.order_items
(
    order_id      INTEGER NOT NULL REFERENCES order_management.orders ON DELETE CASCADE,
    item_id       INTEGER NOT NULL,
    item_name     VARCHAR(100) NOT NULL,
    amount       INTEGER check (amount > 0) NOT NULL,
    price        BIGINT check (price >= 0) NOT NULL,
    PRIMARY KEY (order_id, item_id)
);
