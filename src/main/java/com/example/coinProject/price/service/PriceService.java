package com.example.coinProject.price.service;

import com.example.coinProject.coin.dto.coin.CoinResponse;
import com.example.coinProject.coin.service.CoinService;
import com.example.coinProject.coin.service.Feign;
import com.example.coinProject.price.dto.PriceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.json.JSONParser;
import org.h2.util.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PriceService {

    private final CoinService coinService;

    private double day = 14;

    private double average = (double) 1 / (1 + (day - 1));
    private static final int UNIT = 1;

    private static final int COUNT = 200;

    private final Feign feign;

    private List<Double> upList = new ArrayList<>();
    private List<Double> downList = new ArrayList<>();
    private static final double ZERO = 0;

    private Double getRsi(String market) {


        List<PriceResponse> tradePrices =
                feign.getTradePrice(UNIT, market, COUNT).stream().
                        sorted(Comparator.comparing(PriceResponse::getTimestamp))
                        .collect(Collectors.toList());

        for (int i = 0; i < tradePrices.size() - 1; i++) {
            double gapByTradePrice = tradePrices.get(i + 1).getPrice().doubleValue() - tradePrices.get(i).getPrice().doubleValue();
            if (gapByTradePrice > 0) {
                upList.add(gapByTradePrice);
                downList.add(ZERO);

            } else if (gapByTradePrice < 0) {
                downList.add(gapByTradePrice * -1);
                upList.add(ZERO);
            } else {
                upList.add(ZERO);
                downList.add(ZERO);
            }
        }
        double rs = getAU() / getAD();
        return 100 - (100 / (1 + rs));
    }

    private double getAU() {
        double upEma = 0;
        if (!CollectionUtils.isEmpty(upList)) {
            upEma = upList.get(0).doubleValue();
            if (upList.size() > 1) {
                for (int i = 1; i < upList.size(); i++) {
                    upEma = (upList.get(i).doubleValue() * average) + (upEma * (1 - average));
                }
            }
        }
        return upEma;
    }

    private double getAD() {
        double downEma = 0;
        if (!CollectionUtils.isEmpty(downList)) {
            downEma = downList.get(0).doubleValue();
            if (downList.size() > 1) {
                for (int i = 1; i < downList.size(); i++) {
                    downEma = (downList.get(i).doubleValue() * average) + (downEma * (1 - average));
                }
            }

        }
        return downEma;
    }

    public Map<String, Double> getAllMarketsRsi() throws IOException, InterruptedException {


        Map<String, Double> coinsRsi = new HashMap<>();

        List<CoinResponse> coinResponse = coinService.getCoinResponse();


        for (int i = 0; i < 38; i++) {
            if (coinResponse.get(i).getMarket().startsWith("KRW")) {
                Double rsi = getRsi(coinResponse.get(i).getMarket());
                coinsRsi.put(coinResponse.get(i).getMarket(), rsi);

            }
        }


        // 코인 전체의 rsi 값 가져오기

        return coinsRsi;
        // 30 이하로 추려내기


    }
}