package com.nexdin.clothingstore.jwt;

import com.nexdin.clothingstore.domain.Accounts;
import com.nexdin.clothingstore.services.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String accessToken = headerAuth.substring(7);
            log.debug("Extracted JWT from header: {}", accessToken);
            return accessToken;
        }

        log.warn("No JWT token found in Authorization header");
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Processing authentication for request: {}", request.getRequestURI());

            String accessToken = parseJwt(request);

            if (accessToken != null && jwtUtils.validateToken(accessToken)) {
                String username = jwtUtils.getUserNameFromJwtToken(accessToken);
                log.info("Valid JWT token. Authenticating user: {}", username);

                Accounts account = (Accounts) userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("User '{}' successfully authenticated.", username);
            } else {
                log.warn("Invalid or missing JWT token for request: {}", request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("Failed to set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}