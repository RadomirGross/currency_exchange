package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dao.CurrencyDAO;
import com.gross.currency_exchange.dto.CurrencyDTO;
import com.gross.currency_exchange.mapper.CurrencyMapper;
import com.gross.currency_exchange.model.Currency;

import java.util.List;
import java.util.NoSuchElementException;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDAO currencyDAO;
    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;


    public CurrencyServiceImpl(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        return currencyMapper.toDTOList(currencyDAO.getAllCurrencies());
    }

    @Override
    public CurrencyDTO getCurrencyByCode(String code) {

        Currency currency= currencyDAO.getCurrencyByCode(code);
        if (currency==null)
        {
            throw new NoSuchElementException("Currency by code : "+code+" not found");
        }
        return currencyMapper.toDTO(currency);
    }

    @Override
    public CurrencyDTO addCurrency(String code, String name, String sign) {
        if (code == null || code.isEmpty())
            throw new IllegalArgumentException("Invalid input: 'code' must not be null or empty.");;
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Invalid input: 'name' must not be null or empty.");;
        if (sign == null || sign.isEmpty())
            throw new IllegalArgumentException("Invalid input: 'sign' must not be null or empty.");;
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCode(code);
        currencyDTO.setName(name);
        currencyDTO.setSign(sign);

        Currency currencyEntity = currencyMapper.toEntity(currencyDTO);
        Currency savedCurrency = currencyDAO.addCurrency(currencyEntity);
        return currencyMapper.toDTO(savedCurrency);
    }
}
