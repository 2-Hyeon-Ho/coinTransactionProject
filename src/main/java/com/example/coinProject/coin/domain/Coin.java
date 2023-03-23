package com.example.coinProject.coin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer coinNo;
    @Column(nullable = false)
    private String market;
    @Column(nullable = false)
    private String korName;
    @Column(nullable = false)
    private String engName;
    private Double rsi;
}
