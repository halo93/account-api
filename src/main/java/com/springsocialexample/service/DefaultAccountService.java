package com.springsocialexample.service;

import com.springsocialexample.models.Account;
import com.springsocialexample.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultAccountService implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public Account createAccount(Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public Account getAccountById(Integer id) {
        return accountRepository.findById(id);
    }
}
