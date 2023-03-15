package com.example.coinProject.order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.accessKeys.Keys;
import com.example.coinProject.account.AccountResponse;
import com.example.coinProject.common.JsonUtil;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PostOrders {
    private RestTemplate restTemplate = new RestTemplate();

    public List<OrderResponse> order() throws NoSuchAlgorithmException, UnsupportedEncodingException {


        HashMap<String, String> params = new HashMap<>();
        params.put("market", "KRW-BTC");
        params.put("side", "bid");
        params.put("volume", null);
        params.put("price", "6000");
        params.put("ord_type", "market");

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256("FhiLp0Js9yD8RbckwVjsmBSRc7x3zxKBxrigbOlY");
        String jwtToken = JWT.create()
                .withClaim("access_key", "Dzk2wv7sOKLYsP1Oz1xmnsVPaxKEMUMtW7lyxaFv")
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

//        try {
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpPost request = new HttpPost(
//                    "https://api.upbit.com" + "/v1/orders");
//            request.setHeader("Content-Type", "application/json");
//            request.addHeader("Authorization", authenticationToken);
//            request.setEntity(new StringEntity(JsonUtil.toJson(params)));
//
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//
//            System.out.println("asd "  + EntityUtils.toString(entity, "UTF-8"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", authenticationToken);


        URI uri = URI.create("https://api.upbit.com" + "/v1/orders");

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(httpHeaders), String.class);

        return JsonUtil.listFromJson(response.getBody(), OrderResponse.class);

    }

}
