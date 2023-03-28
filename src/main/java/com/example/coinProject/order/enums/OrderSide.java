package com.example.coinProject.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderSide {
    ASK("ask","매도"),
    BID("bid","매수");

    private String typeInEnglish;

    private String typeInKorean;


}
