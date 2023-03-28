package com.example.coinProject.coinManagement.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinManagementRequest {

    private String market;
    private BigDecimal autoAskRsi;
    private BigDecimal autoBidRsi;
    private Integer autoAskRate;
    private Integer autoBidRate;
}
