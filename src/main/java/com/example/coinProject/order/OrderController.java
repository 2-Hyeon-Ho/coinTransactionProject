package com.example.coinProject.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final PostOrders postOrders;

    @GetMapping
    public void order() throws Exception {
        postOrders.order();

    }

}
