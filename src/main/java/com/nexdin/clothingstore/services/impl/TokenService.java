package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Tokens;
import com.nexdin.clothingstore.repository.ITokenRepository;
import com.nexdin.clothingstore.services.ITokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TokenService implements ITokenService {
    @Autowired
    private ITokenRepository tokenRepository;

    @Override
    public Optional<Tokens> findTokenByAccessToken(String accessToken) {
        log.info("Searching for token by accessToken: {}", accessToken);
        Optional<Tokens> token = tokenRepository.findByAccessToken(accessToken);

        if (token.isPresent()) {
            log.info("Token found for accessToken: {}", accessToken);
        } else {
            log.warn("No token found for accessToken: {}", accessToken);
        }

        return token;
    }

    @Override
    public Optional<Tokens> findTokenByRefreshToken(String refreshToken) {
        log.info("Searching for token by refreshToken: {}", refreshToken);
        Optional<Tokens> token = tokenRepository.findByRefreshToken(refreshToken);

        if (token.isPresent()) {
            log.info("Token found for refreshToken: {}", refreshToken);
        } else {
            log.warn("No token found for refreshToken: {}", refreshToken);
        }

        return token;
    }

    @Override
    public boolean existsById(String tokenID) {
        return tokenRepository.existsById(tokenID);
    }

    @Override
    public void saveToken(Tokens token) {
        tokenRepository.save(token);
        log.info("Token saved successfully");
    }

    @Override
    public boolean isRevokedAccessToken(String accessToken) {
        log.info("Checking if accessToken is revoked: {}", accessToken);
        boolean revoked = findTokenByAccessToken(accessToken).orElseThrow(
                () -> {
                    log.error("AccessToken not found: {}", accessToken);
                    return new UsernameNotFoundException("Not Found with access token: " + accessToken);
                }
        ).isRevoked();

        log.info("AccessToken {} revoked status: {}", accessToken, revoked);
        return revoked;
    }
}
