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
    private final String BASE_CURRENCY_CODE = "USD";

    public ExchangeResultServiceImpl() {
        this.currencyService = new CurrencyServiceImpl(new CurrencyDAOImpl());
        this.exchangeRateService = new ExchangeRateServiceImpl(new ExchangeRateDAOImpl(), currencyService);
    }

    @Override
    public ExchangeResult getExchangeResult(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        validateInput(baseCurrencyCode, targetCurrencyCode, amount);
        try {
            BigDecimal rate;
            if ((rate = getDirectRate(baseCurrencyCode, targetCurrencyCode)) != null)
                return buildExchangeResult(baseCurrencyCode, targetCurrencyCode, amount, rate);

            if((rate=getReverseRate(baseCurrencyCode, targetCurrencyCode)) != null)
                return buildExchangeResult(baseCurrencyCode, targetCurrencyCode, amount, rate);

            if((rate=getRateUsingUSDCurrency(baseCurrencyCode, targetCurrencyCode)) != null)
                return buildExchangeResult(baseCurrencyCode, targetCurrencyCode, amount, rate);
        }catch (EntityNotFoundException e)
        {
            return null;
        }
        return null;
    }

    private void validateInput(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        if (baseCurrencyCode == null || baseCurrencyCode.isEmpty() ||
                targetCurrencyCode == null || targetCurrencyCode.isEmpty() || amount == null) {
            throw new IllegalArgumentException("Currency codes and amount must not be null.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private ExchangeResult buildExchangeResult(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount, BigDecimal rate) {
        ExchangeResult result = new ExchangeResult();
        result.setBaseCurrency(currencyMapper.toEntity(currencyService.getCurrencyByCode(baseCurrencyCode)));
        result.setTargetCurrency(currencyMapper.toEntity(currencyService.getCurrencyByCode(targetCurrencyCode)));
        result.setAmount(amount);
        result.setRate(rate);
        result.setConvertedAmount(amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    private BigDecimal getDirectRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRate exchangeRate = exchangeRateMapper.
                    toExchangeRate(exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode));
            return exchangeRate.getRate();
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private BigDecimal getReverseRate(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRate exchangeRate = exchangeRateMapper
                    .toExchangeRate(exchangeRateService.getExchangeRate(targetCurrencyCode, baseCurrencyCode));
            return BigDecimal.ONE.divide(exchangeRate.getRate(), MathContext.DECIMAL64);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private BigDecimal getRateUsingUSDCurrency(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ExchangeRate baseExchangeRateFromUSD = exchangeRateMapper
                    .toExchangeRate(exchangeRateService.getExchangeRate(BASE_CURRENCY_CODE, baseCurrencyCode));

            ExchangeRate targetExchangeRateFromUSD = exchangeRateMapper
                    .toExchangeRate(exchangeRateService.getExchangeRate(BASE_CURRENCY_CODE, targetCurrencyCode));

            if (baseExchangeRateFromUSD != null && targetExchangeRateFromUSD != null)
                return targetExchangeRateFromUSD.getRate()
                        .divide(baseExchangeRateFromUSD.getRate(), 6, RoundingMode.HALF_UP);
        } catch (EntityNotFoundException e) {
            return null;
        }
        return null;
    }

}