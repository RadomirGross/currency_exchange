package com.gross.currency_exchange.mapper;

import com.gross.currency_exchange.model.ExchangeRate;
import org.mapstruct.factory.Mappers;

public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRate toExchangeRate(ExchangeRate exchangeRate);
}
