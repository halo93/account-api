package com.springsocialexample.repository;

import com.springsocialexample.models.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private AccountRepository accountRepository;

	public UserDetailsServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(account.getUsername(), account.getPassword(), emptyList());
	}
}
