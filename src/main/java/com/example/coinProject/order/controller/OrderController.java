package com.example.coinProject.order.controller;

import com.example.coinProject.account.dto.AccountResponse;
import com.example.coinProject.account.service.AccountService;
import com.example.coinProject.order.service.PostOrdersService;
import com.example.coinProject.price.service.PriceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final PostOrdersService postOrdersService;

    private final PriceService priceService;

    private final AccountService accountService;


    @GetMapping("/sell")
    public void orderSell() throws Exception {

        String volume = accountService.accounts().
                stream().filter(it -> it.getCurrency().equals("BTC")).map(AccountResponse::getBalance)
                .findAny().orElseThrow(); // 계좌에서 마켓 volume 가져옴


        postOrdersService.order("KRW-BTC", "ask", volume, null, "market");
    }

    @GetMapping("/buy")
    public void orderBuy() throws Exception {

        String price = accountService.accounts().stream().
                filter(it -> it.getCurrency().equals("KRW")).
                map(AccountResponse::getBalance).
                findAny().orElseThrow();    // 계좌에서 잔액 가져옴



        postOrdersService.order("KRW-BTC", "bid", null, getString(price), "price");


    }


    private String getString(String price) {
        double i = Double.parseDouble(price);

        int j = (int) i - 50;  // 추후에는 50% 로 하기

        String priceValue = String.valueOf(j);  // price 소수점 제거 작업
        return priceValue;
    }

}
