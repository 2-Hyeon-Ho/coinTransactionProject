package com.example.coinProject.coin.controller;

import com.example.coinProject.coin.dto.coin.CoinResponse;
import com.example.coinProject.coin.service.CoinService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
@Slf4j
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    public List<CoinResponse> getMarket() {

        return coinService.getCoinResponse();
    }

    @GetMapping("/saveCoin")
    public List<CoinResponse> saveMarket(){
        return coinService.saveMarket();
    }





}
