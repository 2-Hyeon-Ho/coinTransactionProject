//package com.example.coinProject.order;
//
//import com.example.coinProject.common.EnumInterface;
//import com.example.coinProject.common.EnumInterfaceConverter;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public enum OrderSideType implements EnumInterface {
//    ASK("ask", "매도"),
//    BID("bid", "매수");
//
//    private String type;
//    private String name;
//
//    public static OrderSideType find(String type) {
//        return EnumInterface.find(type, values());
//    }
//
//    @JsonCreator
//    public static OrderSideType findToNull(String type) {
//        return EnumInterface.findToNull(type, values());
//    }
//
//    @javax.persistence.Converter(autoApply = true)
//    public static class Converter extends EnumInterfaceConverter<OrderSideType> {
//        public Converter() {
//            super(OrderSideType.class);
//        }
//    }
//
//
//}
