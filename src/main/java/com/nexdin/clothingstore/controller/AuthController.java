package com.nexdin.clothingstore.controller;

import com.nexdin.clothingstore.domain.Accounts;
import com.nexdin.clothingstore.payload.request.AuthRequest;
import com.nexdin.clothingstore.payload.request.TokenRequest;
import com.nexdin.clothingstore.payload.response.AuthResponse;
import com.nexdin.clothingstore.payload.response.Response;
import com.nexdin.clothingstore.services.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<?>> register(@RequestBody AuthRequest request) {
        Accounts account = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Register Successfully")
                        .timestamp(LocalDateTime.now())
                        .result(account)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        AuthResponse body = authService.login(request, httpRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Login Successfully")
                        .result(body)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response<?>> refreshToken(@RequestBody TokenRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Refresh Token Successfully")
                        .result(authService.refreshToken(request, httpRequest))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<Response<?>> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Logout Successfully")
                        .result(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/revoked-token")
    public ResponseEntity<Response<?>> revokedToken(@RequestBody TokenRequest request) {
        authService.revokedToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Revoked Token Successfully")
                        .result(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}