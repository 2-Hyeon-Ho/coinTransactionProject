package com.example.coinProject.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class AccountResponse {

    private String currency;

    private String balance;

    private String locked;

    @JsonProperty("avg_buy_price")
    private String averageBuyPrice;

    @JsonProperty("avg_buy_price_modified")
    private boolean averageBuyPriceModified;

    @JsonProperty("unit_currency")
    private String unitCurrency;


    @Override
    public String toString() {
        return "AccountResponse{" +
                "화폐'" + currency + '\'' +
                ", 잔액 " + balance + '\'' +
                ", locked='" + locked + '\'' +
                ", 평균매수가" + averageBuyPrice + '\'' +
                ", averageBuyPriceModified=" + averageBuyPriceModified +
                ", 화폐단위'" + unitCurrency + '\'' +
                '}';
    }
}
