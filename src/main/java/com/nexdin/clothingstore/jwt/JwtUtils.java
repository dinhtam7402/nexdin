package com.nexdin.clothingstore.jwt;

import com.nexdin.clothingstore.domain.Accounts;
import com.nexdin.clothingstore.services.ITokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    @Value("${nexdin.app.jwtSecret}")
    private String jwtSecret;

    @Value("${nexdin.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    private ITokenService tokenService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_16);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Authentication authentication) {
        Accounts account = (Accounts) authentication.getPrincipal();
        String accessToken = Jwts.builder()
                .subject(account.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info("Generated access token for username: {}", account.getUsername());
        return accessToken;
    }

    public String generateJwtRefreshToken(Accounts account) {
        String accessToken = Jwts.builder()
                .subject(account.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        log.info("Generated refresh token for username: {}", account.getUsername());
        return accessToken;
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            String username = Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(token).getPayload().getSubject();
            log.info("Extracted username '{}' from access token", username);
            return username;
        } catch (JwtException e) {
            log.error("Failed to extract username from access token: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT Token");
        }

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            if (tokenService.isRevokedAccessToken(token)) {
                log.warn("Token has been revoked: {}", token);
                throw new AuthenticationCredentialsNotFoundException("JWT has been revoked");
            }
            log.info("JWT token is valid");
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
        }
    }
}
