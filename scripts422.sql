-- Таблица машина
CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Таблица человек
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT CHECK (age > 0),
    has_license BOOLEAN NOT NULL,
    car_id INT REFERENCES car (id)
);
