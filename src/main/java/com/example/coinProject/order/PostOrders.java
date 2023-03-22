package com.example.coinProject.order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.accessKeys.Keys;
import com.example.coinProject.common.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

@Service
@RequiredArgsConstructor
public class PostOrders {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private Keys keys = new Keys();

    public void order() throws Exception {

        HashMap<String, String> params = new HashMap<>();
        params.put("market", "KRW-BTC");
        params.put("side", "bid");
        params.put("volume", null);
        params.put("price", "6000");
        params.put("ord_type", "price");

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        System.out.println("queryString = " + queryString);

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC512("rhWxcZXT2VBC5xNR4HJO9r1YafH5b9rLNxDhOEQV");
        String jwtToken = JWT.create()
                .withClaim("access_key", "fbKE9lhKEA3IgyiQMkFBLimqRUMQp2xwprZNvCiv")
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(AUTHORIZATION, authenticationToken);

        URI uri = URI.create("https://api.upbit.com" + "/v1/orders" + "?" + queryString);

        System.out.println("httpheaders : " + httpHeaders);

        HttpEntity<String> hashMapHttpEntity = new HttpEntity<>(objectMapper.writeValueAsString(params), httpHeaders);

        System.out.println("hashMapHttpEntity = " + hashMapHttpEntity);

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ResponseEntity<JsonNode> exchange = restTemplate.exchange(uri, HttpMethod.POST, hashMapHttpEntity, JsonNode.class);

        System.out.println("exchange = " + exchange);
//
//        if (exchange.getStatusCode() != HttpStatus.CREATED) {
//            throw new Exception("StatusCode = " + exchange.getStatusCode().value());
//        }


//        ResponseEntity<String> response =
//                restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity(params, httpHeaders), String.class);

//        System.out.println("asd " + EntityUtils.toString(entity, "UTF-8"));

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//        httpHeaders.add("Authorization", authenticationToken);
//        URI uri = URI.create("https://api.upbit.com" + "/v1/accounts");
//
//        httpHeaders.setLocation(uri);


    }

}
