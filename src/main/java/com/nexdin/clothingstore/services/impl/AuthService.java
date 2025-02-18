package com.nexdin.clothingstore.services.impl;

import com.nexdin.clothingstore.domain.Accounts;
import com.nexdin.clothingstore.domain.Tokens;
import com.nexdin.clothingstore.jwt.JwtUtils;
import com.nexdin.clothingstore.payload.request.AuthRequest;
import com.nexdin.clothingstore.payload.request.TokenRequest;
import com.nexdin.clothingstore.payload.response.AuthResponse;
import com.nexdin.clothingstore.services.IAccountService;
import com.nexdin.clothingstore.services.IAuthService;
import com.nexdin.clothingstore.services.ITokenService;
import com.nexdin.clothingstore.utils.IDGenerate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class AuthService implements IAuthService {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${nexdin.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${nexdin.app.jwtExpirationRefreshMs}")
    private int jwtExpirationRefreshMs;

    @Override
    public Accounts register(AuthRequest request) {
        String username = request.getUsername();
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("Password encrypted successfully for username: {}", username);

        if (accountService.existsByUsername(username)) {
            log.warn("Registration failed: Username '{}' already exists", username);
            throw new RuntimeException("Registration Failed: Provided username already exists. Try sign in or provide another username.");
        }

        Accounts account = accountService.createAccount(username, encryptedPassword);
        log.info("Account registered successfully with username: {}", username);

        return account;
    }

    @Override
    public AuthResponse login(AuthRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        log.info("Authentication successful for username: {}", request.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authentication set in SecurityContextHolder for username: {}", request.getUsername());

        Accounts account = (Accounts) authentication.getPrincipal();
        log.debug("Retrieved account details for username: {}", account.getUsername());

        String accessToken = jwtUtils.generateJwtToken(authentication);
        String refreshToken = IDGenerate.generate();
        log.info("Generated access token and refresh token for username: {}", account.getUsername());

        LocalDateTime expiredAccessToken = new Date((new Date()).getTime() + jwtExpirationMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiredRefreshToken = new Date((new Date()).getTime() + jwtExpirationRefreshMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.debug("Access token expiry: {}", expiredAccessToken);
        log.debug("Refresh token expiry: {}", expiredRefreshToken);

        String tokenID;
        do {
            tokenID = IDGenerate.generate();
        } while (tokenService.existsById(tokenID));

        log.debug("Generated unique token ID: {}", tokenID);

        Tokens token = Tokens.builder()
                .tokenID(tokenID)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredAccessToken(expiredAccessToken)
                .expiredRefreshToken(expiredRefreshToken)
                .deviceName(httpRequest.getHeader("User-Agent"))
                .ipAddress(httpRequest.getRemoteAddr())
                .issuedAt(LocalDateTime.now())
                .account(account)
                .build();
        log.info("Token object created for username: {}", account.getUsername());

        tokenService.saveToken(token);
        log.info("Token saved successfully for username: {}", account.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(TokenRequest refreshToken, HttpServletRequest httpRequest) {
        Tokens token = tokenService.findTokenByRefreshToken(refreshToken.getToken())
                .orElseThrow(() -> {
                    log.warn("Refresh token not found: {}", refreshToken.getToken());
                    return new UsernameNotFoundException("Not Found with refresh token: " + refreshToken.getToken());
                });

        if (token.isRevoked()) {
            log.warn("Attempt to use revoked refresh token: {}", refreshToken.getToken());
            throw new RuntimeException("JWT has been revoked.Please make a new login request.");
        }

        if (token.getExpiredRefreshToken().compareTo(LocalDateTime.now()) < 0) {
            token.setRevoked(true);
            tokenService.saveToken(token);
            log.warn("Refresh token expired: {}", refreshToken.getToken());
            throw new RuntimeException("Refresh token was expired. Please make a new login request.");
        }

        token.setRevoked(true);
        tokenService.saveToken(token);

        String accessToken = jwtUtils.generateJwtRefreshToken(token.getAccount());
        String newRefreshToken = IDGenerate.generate();
        LocalDateTime expiredAccessToken = new Date((new Date()).getTime() + jwtExpirationMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiredRefreshToken = new Date((new Date()).getTime() + jwtExpirationRefreshMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        log.debug("Generated new access token for user '{}'", token.getAccount().getUsername());
        log.debug("Generated new refresh token: {}", newRefreshToken);

        Tokens newToken = Tokens.builder()
                .tokenID(IDGenerate.generate())
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiredAccessToken(expiredAccessToken)
                .expiredRefreshToken(expiredRefreshToken)
                .deviceName(httpRequest.getHeader("User-Agent"))
                .ipAddress(httpRequest.getRemoteAddr())
                .issuedAt(LocalDateTime.now())
                .account(token.getAccount())
                .build();

        tokenService.saveToken(newToken);
        log.info("New refresh token generated successfully for user '{}'", token.getAccount().getUsername());

        return new AuthResponse(accessToken, newRefreshToken);
    }

    @Override
    public void revokedToken(TokenRequest request) {
        Tokens token = tokenService.findTokenByAccessToken(request.getToken())
                .orElseThrow(() -> {
                    log.warn("Attempt to revoke a non-existent token: {}", request.getToken());
                    return new IllegalArgumentException("Token does not exist.");
                });

        if (token.isRevoked()) {
            log.warn("Attempt to revoke an already revoked token: {}", request.getToken());
            throw new IllegalStateException("The token has been revoked previously.");
        }

        token.setRevoked(true);
        tokenService.saveToken(token);
        log.info("Token successfully revoked: {}", request.getToken());
    }

    @Override
    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Logout attempt failed: No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Tokens token = tokenService.findTokenByAccessToken(accessToken)
                .orElseThrow(() -> {
                    log.warn("Logout attempt with non-existent token: {}", accessToken);
                    return new RuntimeException("Token not found.");
                });

        token.setRevoked(true);
        tokenService.saveToken(token);
        log.info("Token revoked successfully during logout: {}", accessToken);

        SecurityContextHolder.clearContext();
        log.info("Security context cleared successfully.");
    }
}
