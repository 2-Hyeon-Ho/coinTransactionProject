package com.example.coinProject.coinManagement.dto;

import com.example.coinProject.coinManagement.domain.CoinManagement;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CoinManagementResponse {

    private Long userId;
    private Long coinId;
    private BigDecimal autoAskRsi;
    private BigDecimal autoBidRsi;
    private Integer autoAskRate;
    private Integer autoBidRate;

    public CoinManagementResponse(CoinManagement coinManagement) {
        this.userId = coinManagement.getPk().getUserId();
        this.coinId = coinManagement.getPk().getCoinId();
        this.autoAskRsi = coinManagement.getAutoAskRsi();
        this.autoBidRsi = coinManagement.getAutoBidRsi();
        this.autoAskRate = coinManagement.getAutoAskRate();
        this.autoBidRate = coinManagement.getAutoBidRate();
    }
}
