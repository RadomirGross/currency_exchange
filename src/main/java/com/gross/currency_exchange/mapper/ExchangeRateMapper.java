package com.gross.currency_exchange.mapper;

import com.gross.currency_exchange.dto.ExchangeRateDTO;
import com.gross.currency_exchange.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRate toExchangeRate(ExchangeRateDTO exchangeRateDTO);
    ExchangeRateDTO toExchangeRateDTO(ExchangeRate exchangeRate);
    List<ExchangeRateDTO> toExchangeRateDTOList(List<ExchangeRate> exchangeRateList);
    List<ExchangeRate> toExchangeRateList(List<ExchangeRateDTO> exchangeRateDTOList);
}
