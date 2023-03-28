package com.example.coinProject.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Deque;

@Data
@Repository
public class TradeResult {

    private String type; // trade
    private String code; // KRW-BTC
    @JsonProperty("trade_price")
    private BigDecimal tradePrice; // 체결 가격

    private String change;

    @JsonProperty("change_price")
    private BigDecimal changePrice;
}
