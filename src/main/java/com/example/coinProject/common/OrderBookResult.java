package com.example.coinProject.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderBookResult {

    private String type;
    private String code;
    @JsonProperty("ask_price")
    private Double askPrice;
    @JsonProperty("bid_price")
    private Double bidPrice;
}
