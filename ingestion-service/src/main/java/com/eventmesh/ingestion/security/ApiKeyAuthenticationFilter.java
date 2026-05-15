package com.eventmesh.ingestion.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Legacy filter for extracting and validating API Key from HTTP header.
 * Expects API key in 'X-API-Key' header.
 *
 * NOTE: This class used to be a spring-managed @Component which caused startup
 * failures because it required an AuthenticationManager bean that isn't
 * available by default. The application now uses `ApiKeyAuthFilter` instead
 * (see com.eventmesh.ingestion.auth.filter.ApiKeyAuthFilter). To avoid an
 * accidental eager instantiation by component scanning we intentionally
 * do NOT annotate this class with @Component.
 */
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final String API_KEY_HEADER = "X-API-Key";

    public ApiKeyAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey != null && !apiKey.isEmpty()) {
            try {
                ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(apiKey, null, null, null);
                var authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.debug("API Key authentication failed: " + e.getMessage());
                // Continue without authentication - will be handled by @PreAuthorize
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip filter for health and public endpoints
        String path = request.getRequestURI();
        return path.startsWith("/health") ||
               path.startsWith("/actuator") ||
               path.startsWith("/swagger") ||
               path.startsWith("/api-docs");
    }
}

