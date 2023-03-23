package com.example.coinProject.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TickerResult {

    private String type; // trade
    private String code; // KRW-BTC
    @JsonProperty("trade_price")
    private BigDecimal tradePrice; // 체결 가격

    private String change;

    @JsonProperty("change_price")
    private BigDecimal changePrice;

    private Long timestamp;
}
