package com.example.coinProject.common;

public enum TradeType implements EnumInterface {

    TICKER("ticker", "현재가"),
    TRADE("trade", "체결"),
    ORDERBOOK("orderbook", "호가");


    TradeType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private String type;
    private String name;

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
