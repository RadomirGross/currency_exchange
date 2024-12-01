package com.gross.currency_exchange.dao;

import com.gross.currency_exchange.model.Currency;
import com.gross.currency_exchange.model.ExchangeRate;


import java.util.List;

public interface ExchangeRateDAO {
    List<ExchangeRate> getAllExchangeRates();
    ExchangeRate getExchangeRate(Currency baseCurrency, Currency targetCurrency);
    ExchangeRate addExchangeRate(ExchangeRate exchangeRate);
    void updateExchangeRate(ExchangeRate exchangeRate);
}
