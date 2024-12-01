package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dao.CurrencyDAOImpl;
import com.gross.currency_exchange.dao.ExchangeRateDAOImpl;
import com.gross.currency_exchange.dto.ExchangeResult;
import com.gross.currency_exchange.mapper.CurrencyMapper;
import com.gross.currency_exchange.mapper.ExchangeRateMapper;
import com.gross.currency_exchange.model.ExchangeRate;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExchangeResultServiceImpl implements ExchangeResultService {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;

    public ExchangeResultServiceImpl() {
        this.currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        this.exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), currencyService);
    }

    @Override
    public ExchangeResult getExchangeResult(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        if (baseCurrencyCode == null || baseCurrencyCode.isEmpty() ||
                targetCurrencyCode == null || targetCurrencyCode.isEmpty() || amount == null) {
            throw new IllegalArgumentException("Currency codes and amount must not be null.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        ExchangeResult exchangeResult = new ExchangeResult();
        exchangeResult.setBaseCurrency(currencyMapper.toEntity(currencyService.getCurrencyByCode(baseCurrencyCode)));
        exchangeResult.setTargetCurrency(currencyMapper.toEntity(currencyService.getCurrencyByCode(targetCurrencyCode)));
        exchangeResult.setAmount(amount);
        System.out.println(exchangeResult.getBaseCurrency());
        System.out.println(exchangeResult.getTargetCurrency());
        try {
            ExchangeRate exchangeRate = exchangeRateMapper.
                    toExchangeRate(exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode));
            exchangeResult.setRate(exchangeRate.getRate());
            exchangeResult.setConvertedAmount(amount.multiply(exchangeResult.getRate()));
            return exchangeResult;
        } catch (EntityNotFoundException e) {
            try {
                ExchangeRate exchangeRate = exchangeRateMapper
                        .toExchangeRate(exchangeRateService.getExchangeRate(targetCurrencyCode, baseCurrencyCode));
                if (exchangeRate != null) {
                    exchangeResult.setRate(BigDecimal.ONE.divide(exchangeRate.getRate(), MathContext.DECIMAL64));
                    exchangeResult.setConvertedAmount(amount.multiply(exchangeResult.getRate()));
                    return exchangeResult;
                }
            } catch (EntityNotFoundException x) {
                try {
                    ExchangeRate baseExchangeRateFromUSD = exchangeRateMapper
                            .toExchangeRate(exchangeRateService.getExchangeRate("USD", baseCurrencyCode));

                    ExchangeRate targetExchangeRateFromUSD = exchangeRateMapper
                            .toExchangeRate(exchangeRateService.getExchangeRate("USD", targetCurrencyCode));

                    if (baseExchangeRateFromUSD != null && targetExchangeRateFromUSD != null) {
                        exchangeResult.setRate(targetExchangeRateFromUSD.getRate()
                                .divide(baseExchangeRateFromUSD.getRate(), 6, RoundingMode.HALF_UP));

                        exchangeResult.setConvertedAmount(amount.multiply(exchangeResult.getRate().setScale(2, RoundingMode.HALF_UP)));
                        return exchangeResult;
                    }
                } catch (EntityNotFoundException z) {
                    return null;
                }
            }
        }
        return null;
    }
}