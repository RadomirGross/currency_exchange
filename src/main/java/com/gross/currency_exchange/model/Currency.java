package com.gross.currency_exchange.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "\"Currencies\"") // Используем двойные кавычки
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private int id;

    @Column(name = "\"Code\"", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "\"FullName\"", nullable = false)
    private String name;

    @Column(name = "\"Sign\"", length = 10)
    private String sign;

    public Currency(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }




}
