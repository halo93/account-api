package com.springsocialexample.repository;

import com.springsocialexample.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findById(Integer id);
    Account findByUsername(String username);
}
