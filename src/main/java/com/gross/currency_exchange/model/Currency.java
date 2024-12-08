package com.gross.currency_exchange.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Entity
@Table(name = "currencies")
@ToString
@NoArgsConstructor
public class Currency {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "fullname", nullable = false)
    private String name;

    @Column(name = "sign", length = 10)
    private String sign;

    public Currency(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }


}
