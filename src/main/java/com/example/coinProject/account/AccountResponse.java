package com.example.coinProject.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class AccountResponse {
    @JsonProperty("Source")
    private List<AccountResponse> accountResponseList;

    private String currency;

    private String balance;

    private String locked;

    @JsonProperty("avg_buy_price")
    private String averageBuyPrice;

    @JsonProperty("avg_buy_price_modified")
    private boolean averageBuyPriceModified;

    @JsonProperty("unit_currency")
    private String unitCurrency;


}
