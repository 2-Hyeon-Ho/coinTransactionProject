package com.example.coinProject.account.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.coinProject.accessKeys.Keys;
import com.example.coinProject.account.dto.AccountResponse;
import com.example.coinProject.common.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private Keys keys = new Keys();
    // access : bj4TbzRxlxOIlfxf4iATx0CL6McBN0xy8keGPs1A
    // secret : K3aJadDsGdYd4e4CTkudx1O8lJx8UGdTqiGafmUN
    private RestTemplate restTemplate = new RestTemplate();

    public List<AccountResponse> accounts() {


        Algorithm algorithm = Algorithm.HMAC256(keys.getSsafySecretKey());
        String jwtToken = JWT.create()
                .withClaim("access_key", keys.getSsafyAccessKey())
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
