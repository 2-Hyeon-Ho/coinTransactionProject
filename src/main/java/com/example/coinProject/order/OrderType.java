//package com.example.coinProject.order;
//
//import com.example.coinProject.common.EnumInterface;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public enum OrderType implements EnumInterface {
//    PRICE("price", "시장가매수"),
//    MARKET("market", "시장가매도"), // 매도용
//    LIMIT("limit", "지정가주문"); // 매수용
//
//    private String type;
//    private String name;
//
//    public static OrderType find(String type) {
//        return EnumInterface.find(type, values());
//    }
//
//    @JsonCreator
//    public static OrderType findToNull(String type) {
//        return EnumInterface.findToNull(type, values());
//    }
//}
