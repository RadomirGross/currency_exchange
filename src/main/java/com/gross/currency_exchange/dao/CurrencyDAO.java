package com.gross.currency_exchange.dao;

import com.gross.currency_exchange.model.Currency;

import java.util.List;

public interface CurrencyDAO {
    public List<Currency> getAllCurrencies();
    public Currency getCurrencyByCode(String code);
    public Currency addCurrency(Currency currency);
}
