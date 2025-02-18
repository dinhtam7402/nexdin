package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Tokens;

import java.util.Optional;

public interface ITokenService {
    Optional<Tokens> findTokenByAccessToken(String accessToken);
    Optional<Tokens> findTokenByRefreshToken(String refreshToken);
    boolean existsById(String tokenID);
    void saveToken(Tokens token);
    boolean isRevokedAccessToken(String accessToken);
}
