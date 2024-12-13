package com.gross.currency_exchange.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Data
@NoArgsConstructor
@Entity
@Table(
        name = "\"ExchangeRates\"",
        uniqueConstraints = @UniqueConstraint(columnNames = {"\"BaseCurrencyId\"", "\"TargetCurrencyId\""})
)
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private int id;

    @ManyToOne
    @JoinColumn(name = "\"BaseCurrencyId\"", nullable = false)
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "\"TargetCurrencyId\"", nullable = false)
    private Currency targetCurrency;

    @Column(name = "\"Rate\"", nullable = false, precision = 10, scale = 6)
    private BigDecimal rate;
}