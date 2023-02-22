package com.example.coinProject.coin.service;

import com.example.coinProject.coin.dto.CoinResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "feign", url = "https://api.upbit.com/v1")
public interface Feign {

    @GetMapping("/market/all")
    List<CoinResponse> getMarketCode();

    @GetMapping("/candles/minutes/{unit}")
    List<CoinResponse> getTradePrice(@PathVariable("unit") int unit, @RequestParam(name = "market") String market);
}
