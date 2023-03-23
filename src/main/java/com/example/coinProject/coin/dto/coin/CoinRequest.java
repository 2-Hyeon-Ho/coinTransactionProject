package com.example.coinProject.coin.dto.coin;

import com.example.coinProject.coin.domain.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequest {
    private String market;
    private String korName;
    private String engName;
    private Double rsi;

    public Coin toCoin() {
        return Coin.builder()
                .market(market)
                .korName(korName)
                .engName(engName)
                .rsi(rsi)
                .build();
    }
}
