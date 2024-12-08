package com.gross.currency_exchange.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyDTO {
    private int id;
    private String code;
    private String name;
    private String sign;

}
