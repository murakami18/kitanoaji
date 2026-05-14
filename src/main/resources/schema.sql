DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories CASCADE; 

CREATE TABLE categories (
  id   SERIAL       NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS regions (
  id   SERIAL       NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
  id          SERIAL       NOT NULL,
  name        VARCHAR(100) NOT NULL,
  email       VARCHAR(255) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  category_id INT          NOT NULL,
  region_id   INT          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES categories (id),
  FOREIGN KEY (region_id)   REFERENCES regions (id)
);

CREATE TABLE IF NOT EXISTS products (
  id          SERIAL         NOT NULL,
  name        VARCHAR(255)   NOT NULL,
  weight      DECIMAL(10, 2) NOT NULL,
  price       DECIMAL(10, 2) NOT NULL,
  category_id INT            NOT NULL,
  region_id   INT            NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES categories (id),
  FOREIGN KEY (region_id)   REFERENCES regions (id)
);

CREATE TABLE IF NOT EXISTS carts (
  id          SERIAL      NOT NULL,
  user_id     INT         NOT NULL,
  game_result VARCHAR(50),
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS cart_items (
  cart_id    INT NOT NULL,
  product_id INT NOT NULL,
  quantity   INT NOT NULL DEFAULT 1,
  PRIMARY KEY (cart_id, product_id),
  FOREIGN KEY (cart_id)    REFERENCES carts (id),
  FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE IF NOT EXISTS orders (
  id           SERIAL    NOT NULL,
  user_id      INT       NOT NULL,
  created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  delivered_at TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS order_items (
  id            SERIAL         NOT NULL,
  order_id      INT            NOT NULL,
  product_id    INT            NOT NULL,
  product_price DECIMAL(10, 2) NOT NULL,
  quantity      INT            NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id)   REFERENCES orders (id),
  FOREIGN KEY (product_id) REFERENCES products (id)
);