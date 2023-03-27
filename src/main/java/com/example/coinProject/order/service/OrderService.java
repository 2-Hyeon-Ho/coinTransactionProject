package com.example.coinProject.order.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.accessKeys.Keys;
import com.example.coinProject.account.dto.AccountResponse;
import com.example.coinProject.account.service.AccountService;
import com.example.coinProject.coin.dto.coin.CoinResponse;
import com.example.coinProject.coin.repository.CoinRepository;
import com.example.coinProject.common.JsonUtil;
import com.example.coinProject.order.dto.OrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final RestTemplate restTemplate;

    private final AccountService accountService;

    private final CoinRepository coinRepository;

    private HashMap<String, String> params = new HashMap<>();


    private void order(String market, String side, int percentage) throws Exception {


        params.put("market", market);
        params.put("side", side);

        if (side.equals("bid")) {

            params.put("volume", null);
            params.put("price", getConvertedPrice(getPrice("KRW"), percentage));
            params.put("ord_type", "price");


        } else {
            params.put("volume", getVolume());
            params.put("price", null);
            params.put("ord_type", "market");


        }


        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC512(Keys.hhSecretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", Keys.hhAccessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(AUTHORIZATION, authenticationToken);

        URI uri = URI.create("https://api.upbit.com" + "/v1/orders" + "?" + queryString);

        ResponseEntity<String> response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                new HttpEntity(params, httpHeaders),
                String.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            log.error("StatusCode : {}", response.getStatusCode().value());
            throw new Exception("StatusCode = " + response.getStatusCode().value());
        }
        OrderResponse orderResponse = JsonUtil.fromJson(response.getBody(), OrderResponse.class);

    }

    @Scheduled(fixedDelay = 1000)

    public void Order() throws Exception {  //무한 반복용

        //DB에서 자동 매매 걸어둔 코인 전부 조회해서 List<CoinResponse>타입으로 반환
        List<CoinResponse> coinResponseList = new ArrayList<>();
        List<String> markets = new ArrayList<>();
        //이런식으로 돌림
        for (CoinResponse coinResponse : coinResponseList) {
            markets.add(coinResponse.getMarket());
        }


        for (String market : markets) {
            CoinResponse coinByMarket =
                coinRepository.findCoinByMarket(market).get();
            if (coinByMarket.getRsi() < 33) {
                order(market, "bid", 50);
            } else if (coinByMarket.getRsi() > 65) {
                order(market, "ask", 50);
            }
        }
    }

    private String getPrice(String KRW) {
        return accountService.accounts().stream().
                filter(it -> it.getCurrency().equals(KRW)).
                map(AccountResponse::getBalance).
                findAny().orElseThrow();

    }


    private String getVolume() {
        return accountService.accounts().
                stream().filter(it -> it.getCurrency().equals("BTC")).map(AccountResponse::getBalance)
                .findAny().orElseThrow();


    }

    private String getConvertedPrice(String price, int percentage) {
        double balance = Double.parseDouble(price);

        double percentageOrder = balance * (percentage / 100.0);
        double possibleOrderBalance = percentageOrder * (100 - 0.0005);

        return String.valueOf(Math.floor(possibleOrderBalance));  // price 소수점 제거 작업
    }
}


