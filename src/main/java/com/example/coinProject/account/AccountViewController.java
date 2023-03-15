package com.example.coinProject.account;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountViewController {

    private final AccountService accountService;

    @GetMapping("/showAccounts")
    public String showAllAccounts(Model model) throws JsonProcessingException {
        List<AccountResponse> accounts = accountService.accounts();
        accounts.remove(0);


        model.addAttribute("account", accountService.accounts());
        model.addAttribute("accounts",accounts);
        return "index";

    }
}
