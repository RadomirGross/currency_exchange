package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dao.ExchangeRateDAO;
import com.gross.currency_exchange.dto.ExchangeRateDTO;
import com.gross.currency_exchange.mapper.CurrencyMapper;
import com.gross.currency_exchange.mapper.ExchangeRateMapper;
import com.gross.currency_exchange.model.Currency;
import com.gross.currency_exchange.model.ExchangeRate;
import jakarta.persistence.EntityNotFoundException;


import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;
    CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
    ExchangeRateDAO exchangeRateDAO;
    CurrencyService currencyService;

    public ExchangeRateServiceImpl(ExchangeRateDAO exchangeRateDAO, CurrencyService currencyService) {
        this.exchangeRateDAO = exchangeRateDAO;
        this.currencyService = currencyService;
    }


    @Override
    public List<ExchangeRateDTO> getAllExchangeRates() {
        return exchangeRateMapper.toExchangeRateDTOList(exchangeRateDAO.getAllExchangeRates());
    }

    @Override
    public ExchangeRateDTO getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {

        Currency baseCurrency = currencyMapper.toEntity(currencyService.getCurrencyByCode(baseCurrencyCode));
        Currency targetCurrency = currencyMapper.toEntity(currencyService.getCurrencyByCode(targetCurrencyCode));
        if (baseCurrency == null)
            throw new IllegalArgumentException("baseCurrencyCode not found");
        if (targetCurrency == null)
            throw new IllegalArgumentException("targetCurrencyCode not found");
        if (baseCurrency.equals(targetCurrency))
            throw new IllegalArgumentException("baseCurrencyCode and targetCurrencyCode are the same");
        ExchangeRateDTO exchangeRateDTO =
                exchangeRateMapper.toExchangeRateDTO(
                        exchangeRateDAO.getExchangeRate(baseCurrency, targetCurrency));
        if (exchangeRateDTO == null) {
                throw new EntityNotFoundException("Exchange rate not found for currencies: "
                        + baseCurrency.getCode() + " -> " + targetCurrency.getCode());
            }
        return exchangeRateDTO;
    }

    @Override
    public ExchangeRateDTO addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        if (baseCurrencyCode == null || baseCurrencyCode.isEmpty())
            throw new IllegalArgumentException("Invalid input: 'baseCurrencyCode' must not be null or empty.");
        ;
        if (targetCurrencyCode == null || targetCurrencyCode.isEmpty())
            throw new IllegalArgumentException("Invalid input: 'targetCurrencyCode' must not be null or empty.");
        ;
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Invalid input: 'rate' must not be null or negative.");
        ;


        ExchangeRateDTO savedExchangeRateDTO = new ExchangeRateDTO();
        savedExchangeRateDTO.setBaseCurrency(currencyService.getCurrencyByCode(baseCurrencyCode));
        savedExchangeRateDTO.setTargetCurrency(currencyService.getCurrencyByCode(targetCurrencyCode));
        savedExchangeRateDTO.setRate(rate);
        return exchangeRateMapper.toExchangeRateDTO
                (exchangeRateDAO.addExchangeRate(exchangeRateMapper
                        .toExchangeRate(savedExchangeRateDTO)));

    }


    @Override
    public ExchangeRateDTO updateExchangeRate(ExchangeRateDTO exchangeRateDTO, BigDecimal rate) {

        exchangeRateDTO.setRate(rate);
        ExchangeRate updatedRate = exchangeRateMapper.toExchangeRate(exchangeRateDTO);
        exchangeRateDAO.updateExchangeRate(updatedRate);
        return exchangeRateMapper.toExchangeRateDTO(updatedRate);
    }
}
