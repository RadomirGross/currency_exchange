CREATE DATABASE IF NOT EXISTS currency_db;

-- Подключение к базе данных
\c currency_db;

CREATE TABLE currencies (

                            id SERIAL PRIMARY KEY,
                            code VARCHAR(10) NOT NULL UNIQUE,
                            fullname VARCHAR(255) NOT NULL,
                            sign VARCHAR(10)
);

INSERT INTO currencies (code, fullname, sign) VALUES
    ('RUB', 'Russian Ruble', '₽')

CREATE TABLE exchange_rates (
                                id SERIAL PRIMARY KEY, -- Айди курса обмена, автоинкремент
                                base_currency_id INT NOT NULL, -- ID базовой валюты
                                target_currency_id INT NOT NULL, -- ID целевой валюты
                                rate DECIMAL(6, 4) NOT NULL, -- Курс обмена базовой валюты к целевой
                                CONSTRAINT fk_base_currency FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
                                CONSTRAINT fk_target_currency FOREIGN KEY (target_currency_id) REFERENCES currencies (id),
                                CONSTRAINT unique_currency_pair UNIQUE (base_currency_id, target_currency_id) -- Уникальность пары валют
);