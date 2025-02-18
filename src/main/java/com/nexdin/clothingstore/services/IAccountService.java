package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Accounts;

public interface IAccountService {
    boolean existsByUsername(String username);
    Accounts createAccount(String username, String encryptedPassword);
}
