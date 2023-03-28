package com.example.coinProject.coin.dto.coin;

import com.example.coinProject.coin.domain.Coin;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CoinResponse {

    private long id;

    private String market;
    @JsonProperty("korean_name")
    private String korName;
    @JsonProperty("english_name")
    private String engName;

    private Double rsi;



    public CoinResponse(Coin coin) {
        this.id = coin.getId();
        this.market = coin.getMarket();
        this.korName = coin.getKorName();
        this.engName = coin.getEngName();
        this.rsi = coin.getRsi();
    }
}
