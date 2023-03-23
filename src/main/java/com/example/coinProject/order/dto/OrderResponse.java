package com.example.coinProject.order.dto;

import com.example.coinProject.order.enums.OrderSide;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class OrderResponse {

    private String uuid;                // 주문의 고유 아이디
    private String side;           // 주문 종류
    @JsonProperty("ord_type")
    private String ordType;            // 주문 방식
    private String price;           // 주문 당시 화폐 가격
    @JsonProperty("avg_price")
    private String avgPrice;        // 체결 가격의 평균가
    private String state;       // 주문 상태
    private String market;              // 마켓의 유일키
    @JsonProperty("created_at")
    private String createdAt;    // KST
    private String volume;          // 사용자가 입력한 주문 양
    @JsonProperty("remaining_volume")
    private String remainingVolume; // 체결 후 남은 주문 양
    @JsonProperty("reserved_fee")
    private String reservedFee;     // 수수료로 예약된 비용
    @JsonProperty("remaining_fee")
    private String remainingFee;    // 남은 수수료
    @JsonProperty("paid_fee")
    private String paidFee;         // 사용된 수수료
    private String locked;          // 거래에 사용중인 비용
    @JsonProperty("executed_volume")
    private String executedVolume;  // 체결된 양
    @JsonProperty("trades_count")
    private Integer tradesCount;        // 해당 주문에 걸린 체결 수
}
