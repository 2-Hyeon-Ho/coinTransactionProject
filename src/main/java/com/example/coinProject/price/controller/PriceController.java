package com.example.coinProject.price.controller;

//import com.example.coinProject.price.service.PriceService;
import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.coin.service.CoinService;
import com.example.coinProject.price.service.PriceService;
import com.example.coinProject.price.service.TradeService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {

    private final CoinService coinService;

    private final PriceService priceService;

    private final TradeService tradeService;
    private final CoinRepository coinRepository;

    @GetMapping
    public Double getCurrentPrice() {
        return tradeService.getCurrentPrice().doubleValue();
    }

    @GetMapping("/rsi")
    public void getRsi() {
        priceService.getAllMarketsRsi();
        coinRepository.findAll();
//        Map<String, Double> allMarketsRsi = priceService.getAllMarketsRsi();

    }

//    @GetMapping("/continue/rsi")
//    public Map<String,Double> getContinueRsi() {
//        coinRepository.findAll();
//
//        while(true) {
//            return priceService.getAllMarketsRsi();
//        }
//    }
}
