package com.springsocialexample.service;

import com.springsocialexample.models.Account;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountById(Integer id);
}
