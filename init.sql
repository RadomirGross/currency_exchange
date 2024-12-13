-- Создание таблицы Currencies
CREATE TABLE "Currencies" (
                              "ID" SERIAL PRIMARY KEY,
                              "Code" VARCHAR(10) NOT NULL UNIQUE,
                              "FullName" VARCHAR(100) NOT NULL,
                              "Sign" VARCHAR(10)
);

-- Создание таблицы ExchangeRates
CREATE TABLE "ExchangeRates" (
                                 "ID" SERIAL PRIMARY KEY,
                                 "BaseCurrencyId" INT NOT NULL,
                                 "TargetCurrencyId" INT NOT NULL,
                                 "Rate" DECIMAL(10, 6) NOT NULL,
                                 CONSTRAINT "FK_Base" FOREIGN KEY ("BaseCurrencyId") REFERENCES "Currencies"("ID"),
                                 CONSTRAINT "FK_Target" FOREIGN KEY ("TargetCurrencyId") REFERENCES "Currencies"("ID"),
                                 CONSTRAINT "UniquePair" UNIQUE ("BaseCurrencyId", "TargetCurrencyId")
);

-- Наполнение таблицы Currencies
INSERT INTO "Currencies" ("Code", "FullName", "Sign") VALUES
                                                          ('USD', 'United States Dollar', '$'),
                                                          ('EUR', 'Euro', '€'),
                                                          ('CNY', 'Chinese Yuan', '¥'),
                                                          ('RUB', 'Russian Ruble', '₽'),
                                                          ('GBP', 'British Pound Sterling', '£'),
                                                          ('JPY', 'Japanese Yen', '¥'),
                                                          ('CAD', 'Canadian Dollar', '$');

-- Наполнение таблицы ExchangeRates
INSERT INTO "ExchangeRates" ("BaseCurrencyId", "TargetCurrencyId", "Rate")
VALUES
    ((SELECT "ID" FROM "Currencies" WHERE "Code" = 'USD'), (SELECT "ID" FROM "Currencies" WHERE "Code" = 'EUR'), 0.85),
    ((SELECT "ID" FROM "Currencies" WHERE "Code" = 'EUR'), (SELECT "ID" FROM "Currencies" WHERE "Code" = 'USD'), 1.18),
    ((SELECT "ID" FROM "Currencies" WHERE "Code" = 'USD'), (SELECT "ID" FROM "Currencies" WHERE "Code" = 'CNY'), 6.5),
    ((SELECT "ID" FROM "Currencies" WHERE "Code" = 'USD'), (SELECT "ID" FROM "Currencies" WHERE "Code" = 'RUB'), 100);
