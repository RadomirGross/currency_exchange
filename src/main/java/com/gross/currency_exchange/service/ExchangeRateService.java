package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {
    public List<ExchangeRateDTO> getAllExchangeRates();
    public ExchangeRateDTO getExchangeRate(String baseCurrencyCode, String targetCurrencyCode);
    public ExchangeRateDTO addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);
    public ExchangeRateDTO updateExchangeRate(ExchangeRateDTO exchangeRateDTO,BigDecimal rate);
}
