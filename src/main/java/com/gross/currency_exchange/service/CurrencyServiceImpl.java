package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dao.CurrencyDAO;
import com.gross.currency_exchange.dto.CurrencyDTO;
import com.gross.currency_exchange.mapper.CurrencyMapper;
import com.gross.currency_exchange.model.Currency;

import java.util.List;

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
        return currencyMapper.toDTO(currencyDAO.getCurrencyByCode(code));
    }

    @Override
    public CurrencyDTO addCurrency(CurrencyDTO currencyDTO) {
        return currencyMapper.toDTO(
                currencyDAO.saveCurrency(currencyMapper.toEntity(currencyDTO)));
    }
}
