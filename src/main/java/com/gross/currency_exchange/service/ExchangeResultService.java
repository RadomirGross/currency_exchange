package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dto.ExchangeResult;

import java.math.BigDecimal;

public interface ExchangeResultService {
ExchangeResult getExchangeResult(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}
