CREATE TABLE "carts" (
    "id"                 SERIAL PRIMARY KEY,
    "user_id"            int       NOT NULL,
    "date"      timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "orders" (
    "id"                 SERIAL PRIMARY KEY,
    "order_number"       bigint,
    "user_id"            int       NOT NULL,
    "dispensary_id"      int       NOT NULL,
    "date"               timestamp NOT NULL DEFAULT (now()),
    "payment_method_id"  int       NOT NULL,
    "delivery_method_id" int       NOT NULL,
    "invoice_id"         int,
    "cart_id"            int       NOT NULL,
    "subtotal_price"     numeric   NOT NULL,
    "excise_tax"         numeric   NOT NULL,
    "sales_tax"          numeric   NOT NULL,
    "total_price"        numeric   NOT NULL
);

CREATE TABLE "order_statuses" (
    "id"        SERIAL PRIMARY KEY,
    "order_id"  int       NOT NULL,
    "status_id" int       NOT NULL,
    "date"      timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "statuses" (
    "id"   SERIAL PRIMARY KEY,
    "name" varchar NOT NULL
);

CREATE TABLE "invoices" (
    "id"   SERIAL PRIMARY KEY,
    "url"  varchar   NOT NULL,
    "date" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "payment_methods" (
    "id"   SERIAL PRIMARY KEY,
    "name" varchar NOT NULL
);

CREATE TABLE "delivery_methods" (
    "id"   SERIAL PRIMARY KEY,
    "name" varchar NOT NULL
);

CREATE TABLE "order_products" (
    "id"                    SERIAL PRIMARY KEY,
    "order_id"              int NOT NULL,
    "dispensary_product_id" int NOT NULL,
    "units"                 int NOT NULL
);

CREATE TABLE "messages" (
    "id"       SERIAL PRIMARY KEY,
    "user_id"  int       NOT NULL,
    "order_id" int       NOT NULL,
    "message"  varchar   NOT NULL,
    "date"     timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "configs" (
    "id"    SERIAL PRIMARY KEY,
    "key"   varchar NOT NULL,
    "value" varchar NOT NULL
);

ALTER TABLE "orders"
    ADD CONSTRAINT fk_orders_payment_methods FOREIGN KEY ("payment_method_id") REFERENCES "payment_methods" ("id");

ALTER TABLE "orders"
    ADD CONSTRAINT fk_orders_delivery_methods FOREIGN KEY ("delivery_method_id") REFERENCES "delivery_methods" ("id");

ALTER TABLE "orders"
    ADD CONSTRAINT fk_orders_invoices FOREIGN KEY ("invoice_id") REFERENCES "invoices" ("id");

ALTER TABLE "orders"
    ADD CONSTRAINT fk_orders_carts FOREIGN KEY ("cart_id") REFERENCES "carts" ("id");

ALTER TABLE "order_statuses"
    ADD CONSTRAINT fk_order_statuses_orders FOREIGN KEY ("order_id") REFERENCES "orders" ("id");

ALTER TABLE "order_statuses"
    ADD CONSTRAINT fk_order_statuses_statuses FOREIGN KEY ("status_id") REFERENCES "statuses" ("id");

ALTER TABLE "order_products"
    ADD CONSTRAINT fk_order_products_orders FOREIGN KEY ("order_id") REFERENCES "orders" ("id");

ALTER TABLE "messages"
    ADD CONSTRAINT fk_messages_orders FOREIGN KEY ("order_id") REFERENCES "orders" ("id");

GRANT ALL PRIVILEGES ON DATABASE ${flyway:database} TO orders_service;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO orders_service;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO orders_service;