package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.repository.IAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = accountRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("User not found with username: {}", username);
            return new UsernameNotFoundException("User Not Found with username: " + username);
        });

        log.info("User '{}' loaded successfully", username);
        return userDetails;
    }
}
