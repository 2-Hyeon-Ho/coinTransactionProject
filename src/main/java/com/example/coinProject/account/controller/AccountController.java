package com.example.coinProject.account.controller;


import com.example.coinProject.account.dto.AccountResponse;
import com.example.coinProject.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String accounts(Model model) {
        List<AccountResponse> accounts = accountService.accounts();

        if (accounts.size() == 0) {
            throw new IllegalArgumentException("Account is not exist");
        }

        model.addAttribute("krw", accountService.getKRWBalance(accounts));

        model.addAttribute("coins", accountService.getCoins(accounts));

        return "account";
    }


}
