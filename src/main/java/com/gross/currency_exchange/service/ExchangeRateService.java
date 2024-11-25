package com.gross.currency_exchange.service;

import com.gross.currency_exchange.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {
    public List<ExchangeRate> getAllExchangeRates();
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency);
    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate);
    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate);
}
