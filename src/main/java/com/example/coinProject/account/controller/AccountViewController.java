package com.example.coinProject.account.controller;


import com.example.coinProject.account.dto.AccountResponse;
import com.example.coinProject.account.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountViewController {

    private final AccountService accountService;

    @GetMapping("/showAccounts")
    public String showAllAccounts(Model model) throws JsonProcessingException {
        List<AccountResponse> accounts = accountService.accounts();
        if(accounts.size() == 0) {
            throw new IllegalArgumentException("Account is not exist");
        }
        accounts.remove(0);

        model.addAttribute("account", accountService.accounts());
        model.addAttribute("accounts",accounts);
        return "account";
    }
}
