package com.gross.currency_exchange.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "exchange_rates",
        uniqueConstraints = @UniqueConstraint(columnNames = {"baseCurrencyId", "targetCurrencyId"})
)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "base_currency_Id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "target_currency_Id", nullable = false)
    private Currency targetCurrency;

    @Column(name = "rate", nullable = false, precision = 10, scale = 6)
    private BigDecimal rate;
}