package com.gross.currency_exchange.service;

import com.gross.currency_exchange.dto.CurrencyDTO;

import java.util.List;

public interface CurrencyService {
    public List<CurrencyDTO> getAllCurrencies();
    public CurrencyDTO getCurrencyByCode(String code);
    public CurrencyDTO addCurrency(String code, String name, String sign);
}
