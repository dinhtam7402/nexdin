package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Tokens, String> {
    Optional<Tokens> findByAccessToken(String accessToken);
    Optional<Tokens> findByRefreshToken(String refreshToken);
}
