DROP TABLE IF EXISTS categoris, regions, users;

CREATE TABLE IF NOT EXISTS categories (
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