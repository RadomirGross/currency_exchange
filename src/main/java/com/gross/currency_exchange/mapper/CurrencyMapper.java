package com.gross.currency_exchange.mapper;

import com.gross.currency_exchange.dto.CurrencyDTO;
import com.gross.currency_exchange.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDTO toDTO(Currency currency);

    Currency toEntity(CurrencyDTO dto);

    List<CurrencyDTO> toDTOList(List<Currency> currencies);

    List<Currency> toEntityList(List<CurrencyDTO> currencyDTOS);

}
