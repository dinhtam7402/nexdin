package com.nexdin.clothingstore.services;

import com.nexdin.clothingstore.domain.Accounts;
import com.nexdin.clothingstore.payload.request.AuthRequest;
import com.nexdin.clothingstore.payload.request.TokenRequest;
import com.nexdin.clothingstore.payload.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    Accounts register(AuthRequest request);
    AuthResponse login(AuthRequest request, HttpServletRequest httpRequest);
    AuthResponse refreshToken(TokenRequest refreshToken, HttpServletRequest httpRequest);
    void revokedToken(TokenRequest request);
    void logout(String accessToken);
}
