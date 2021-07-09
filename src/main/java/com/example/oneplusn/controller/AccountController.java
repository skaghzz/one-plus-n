package com.example.oneplusn.controller;

import com.example.oneplusn.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/account")
    public void insertData() {
        accountService.insertData();
    }

    @GetMapping(value = "/account/fetchjoin")
    public void findAllAccountFetchJoin() {
        accountService.findAllSavginsAccountFetchJoin();
    }
}
