package com.eventmesh.ingestion.security;

import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import com.eventmesh.ingestion.auth.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Custom Authentication Provider for API Key authentication.
 * Validates API keys against the database and provides appropriate authorities.
 */
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyRepository apiKeyRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String credential = (String) authentication.getPrincipal();

        if (credential == null || !credential.contains(":")) {
            throw new BadCredentialsException("Invalid API Key format");
        }

        String[] parts = credential.split(":", 2);
        String keyId = parts[0];
        String secret = parts[1];

        Optional<ApiKeyEntity> apiKeyEntity = apiKeyRepository.findByKeyId(keyId);

        if (apiKeyEntity.isEmpty()) {
            throw new BadCredentialsException("Invalid API Key");
        }

        ApiKeyEntity entity = apiKeyEntity.get();

        if (!entity.isActive()) {
            throw new BadCredentialsException("API Key is inactive");
        }

        if (!passwordEncoder.matches(secret, entity.getApiKeyHash())) {
            throw new BadCredentialsException("Invalid API Key");
        }

        // Record usage
        entity.setLastUsedAt(Instant.now());
        apiKeyRepository.save(entity);

        // Create authorities based on role
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + entity.getRole()));

        // Return authenticated token
        ApiKeyAuthenticationToken token = new ApiKeyAuthenticationToken(
            credential,
            entity.getClientName(),
            entity.getRole(),
            authorities
        );
        token.setAuthenticated(true);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

