package com.example.coinProject.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.common.JsonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {


    // access : bj4TbzRxlxOIlfxf4iATx0CL6McBN0xy8keGPs1A
    // secret : K3aJadDsGdYd4e4CTkudx1O8lJx8UGdTqiGafmUN
    private RestTemplate restTemplate = new RestTemplate();

    public List<AccountResponse> accounts() {


        Algorithm algorithm = Algorithm.HMAC256("FhiLp0Js9yD8RbckwVjsmBSRc7x3zxKBxrigbOlY");
        String jwtToken = JWT.create()
                .withClaim("access_key", "Dzk2wv7sOKLYsP1Oz1xmnsVPaxKEMUMtW7lyxaFv")
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Authorization", authenticationToken);


        URI uri = URI.create("https://api.upbit.com" + "/v1/accounts");

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,new HttpEntity(httpHeaders), String.class);


        return JsonUtil.listFromJson(response.getBody(),AccountResponse.class);
    }


}
