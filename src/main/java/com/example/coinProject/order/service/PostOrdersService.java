package com.example.coinProject.order.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.accessKeys.Keys;
import com.example.coinProject.common.JsonUtil;
import com.example.coinProject.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
public class PostOrdersService {
    private final RestTemplate restTemplate;

    public OrderResponse order(String market, String side, String volume,String price, String ordType) throws Exception {

        HashMap<String, String> params = new HashMap<>();
        params.put("market", market);
        params.put("side", side);
        params.put("volume", volume);
        params.put("price", price);
        params.put("ord_type", ordType);

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC512(Keys.ssafySecretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", Keys.ssafyAccessKey)
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

        log.info("orderResponse : {}", orderResponse);

        return JsonUtil.fromJson(response.getBody(), OrderResponse.class);
    }


}


