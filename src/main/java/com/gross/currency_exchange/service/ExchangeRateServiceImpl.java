package com.gross.currency_exchange.service;

import com.gross.currency_exchange.model.ExchangeRate;

import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        return List.of();
    }

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        return null;
    }

    @Override
    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate) {
        return null;
    }
}
