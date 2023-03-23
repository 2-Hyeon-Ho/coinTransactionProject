package com.example.coinProject.coin.service;

import com.example.coinProject.coin.domain.Coin;
import com.example.coinProject.coin.dto.coin.CoinRequest;
import com.example.coinProject.coin.dto.coin.CoinResponse;
import com.example.coinProject.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CoinService {

    private final CoinRepository coinRepository;
    private final Feign feign;


    @Transactional
    public List<CoinResponse> getCoinResponse() {
        return feign.getMarketCode();
    }

    @Transactional
    public List<CoinResponse> saveMarket() {
        List<CoinResponse> marketCode = feign.getMarketCode();

        List<Coin> coins = new ArrayList<>();

        for (CoinResponse coinResponse : marketCode) {
                CoinRequest coinRequest = new CoinRequest(
                        coinResponse.getMarket(),
                        coinResponse.getEngName(),
                        coinResponse.getKorName(),
                        coinResponse.getRsi()
                );

                Coin coin = coinRepository.save(coinRequest.toCoin());

                coins.add(coin);


        }

        return coins.stream().map(coin -> new CoinResponse(coin)).collect(Collectors.toList());

    }




}
