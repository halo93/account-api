package com.springsocialexample.controllers;

import com.springsocialexample.models.Account;
import com.springsocialexample.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts/create")
    public ResponseEntity<?> create(@RequestBody Account account) {
        return new ResponseEntity<Account>(accountService.createAccount(account), HttpStatus.OK);
    }
}
