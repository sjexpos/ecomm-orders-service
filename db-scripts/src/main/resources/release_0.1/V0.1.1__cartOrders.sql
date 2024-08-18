-- INSERT payment_methods
INSERT INTO payment_methods (id, name) VALUES (1, 'CASH');

-- INSERT delivery_methods
INSERT INTO delivery_methods (id, name) VALUES (1, 'PICK_UP');

-- INSERT statuses
INSERT INTO statuses (id, name) VALUES (1, 'ORDERED');
INSERT INTO statuses (id, name) VALUES (2, 'CONFIRMED');
INSERT INTO statuses (id, name) VALUES (3, 'READY');
INSERT INTO statuses (id, name) VALUES (4, 'DELIVERED');
INSERT INTO statuses (id, name) VALUES (5, 'CANCELED');

-- UPDATE order_products with price per unit so if the price of the publication changes, you still know how much it was.
ALTER TABLE order_products ADD COLUMN price numeric NOT NULL;
ALTER TABLE order_products ADD COLUMN transaction_id int NOT NULL;

-- UPDATE orders with last status
ALTER TABLE orders ADD COLUMN last_status varchar NOT NULL;