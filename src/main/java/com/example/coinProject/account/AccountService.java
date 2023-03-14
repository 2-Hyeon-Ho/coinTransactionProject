package com.example.coinProject.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {


    // access : bj4TbzRxlxOIlfxf4iATx0CL6McBN0xy8keGPs1A
    // secret : K3aJadDsGdYd4e4CTkudx1O8lJx8UGdTqiGafmUN
    private RestTemplate restTemplate = new RestTemplate();

    public AccountResponse accounts() {


        Algorithm algorithm = Algorithm.HMAC256("VXcwtM9Rlx63xFUMeQlGcxQgiJeymG5hDcaRGUEz");
        String jwtToken = JWT.create()
                .withClaim("access_key", "2RGbxSEFVGE10BUhaDtIyIYUkc9WBOQV07V0PSXL")
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.upbit.com" + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();


            String s = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            AccountResponse accountResponse = objectMapper.readValue(s, AccountResponse.class);
            System.out.println("asd" + accountResponse.getBalance());
            return accountResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
