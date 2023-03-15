//package com.example.coinProject.order;
//
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.example.coinProject.accessKeys.Keys;
//import com.example.coinProject.common.JsonUtil;
//import lombok.RequiredArgsConstructor;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//
//    private Keys keys = new Keys();
//
//    public void Order(String volume,
//                      String price,
//                      OrderSideType ordSideType) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("market", "KRW-BTC");
//        params.put("side", ordSideType.getType());
//        switch (ordSideType) {
//            case ASK:
//                // 매도
//                params.put("volume", volume); // 매도수량 필수
//                params.put("ord_type", OrderType.MARKET.getType()); // 시장가 매도
//                break;
//            case BID:
//                // 매수
//                params.put("price", price); // 매수 가격
//                params.put("ord_type", OrderType.PRICE.getType()); // 시장가 매수
//                break;
//            default:
//                return;
//        }
//
//        ArrayList<String> queryElements = new ArrayList<>();
//        for(Map.Entry<String, String> entity : params.entrySet()) {
//            queryElements.add(entity.getKey() + "=" + entity.getValue());
//        }
//
//        String queryString = String.join("&", queryElements.toArray(new String[0]));
//
//        MessageDigest md = MessageDigest.getInstance("SHA-512");
//        md.update(queryString.getBytes("UTF-8"));
//
//        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
//
//        Algorithm algorithm = Algorithm.HMAC256(keys.getSecretKey());
//        String jwtToken = JWT.create()
//                .withClaim("access_key", keys.getAccessKey())
//                .withClaim("nonce", UUID.randomUUID().toString())
//                .withClaim("query_hash", queryHash)
//                .withClaim("query_hash_alg", "SHA512")
//                .sign(algorithm);
//
//        String authenticationToken = "Bearer " + jwtToken;
//
//        try {
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpPost request = new HttpPost("https://api.upbit.com" + "/v1/orders");
//            request.setHeader("Content-Type", "application/json");
//            request.addHeader("Authorization", authenticationToken);
//            request.setEntity(new StringEntity(JsonUtil.toJson(params)));
//
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//
//            System.out.println("asd" + EntityUtils.toString(entity, "UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
