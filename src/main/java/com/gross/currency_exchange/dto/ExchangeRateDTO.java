package com.gross.currency_exchange.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ExchangeRateDTO {
    private int id;
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private BigDecimal rate;

}
