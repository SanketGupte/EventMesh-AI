package com.eventmesh.ingestion.auth.service;

import com.eventmesh.ingestion.auth.dto.ApiKeyCreateResponse;
import com.eventmesh.ingestion.auth.dto.ApiKeyView;
import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import com.eventmesh.ingestion.auth.exception.CustomAuthException;
import com.eventmesh.ingestion.auth.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();

    /*Validate API Key header of format keyId:secret*/
    public ApiKeyEntity validateApiKeyHeader(String header) throws CustomAuthException {
        if (header == null || !header.contains(":")) {
            log.warn("Invalid API key header format");
            throw new CustomAuthException("Invalid API Key format");
        }

        String[] parts = header.split(":", 2);
        String keyId = parts[0];
        String secret = parts[1];

        ApiKeyEntity entity = apiKeyRepository.findByKeyId(keyId)
                .orElseThrow(() -> new CustomAuthException("Invalid Api Key"));

        if (!entity.isActive()){
            log.warn("Inactive api key used: {}", keyId);
            throw new CustomAuthException("API Key is inactive.");
        }

        if (!passwordEncoder.matches(secret, entity.getApiKeyHash())){
            log.warn("Invalid api key secret for keyId {}", keyId);
            throw new CustomAuthException("Invalid Api Key");
        }

        // track usage
        entity.setLastUsedAt(Instant.now());
        apiKeyRepository.save(entity);

        return entity;
    }

    /*Generate new API Key (returns plaintext only once)
      Response contains keyId and plaintext secret.*/
    public ApiKeyCreateResponse generateApiKey(String clientName, String role){
        byte[] random = new byte[32];
        secureRandom.nextBytes(random);
        String plaintext = Base64.getUrlEncoder().withoutPadding().encodeToString(random);

        String hashed = passwordEncoder.encode(plaintext);

        ApiKeyEntity entity = ApiKeyEntity.builder()
                .apiKeyHash(hashed)
                .clientName(clientName)
                .role(role)
                .active(true)
                .build();

        ApiKeyEntity saved = apiKeyRepository.save(entity);
        log.info("Api Key generated for {} with role {} and keyId {}", clientName, role, saved.getKeyId());

        return ApiKeyCreateResponse.builder()
                .keyId(saved.getKeyId())
                .apiKey(plaintext)
                .build();
    }

    /*Deactivate API Key by keyId*/
    public void deactivateKeyById(String keyId){
        ApiKeyEntity entity = apiKeyRepository.findByKeyId(keyId).orElseThrow(() -> new RuntimeException("API Key not found"));

        entity.setActive(false);
        apiKeyRepository.save(entity);
        log.info("API Key deactivated: {}", keyId);
    }

    /*Fetch All API Keys (view DTOs without secrets)*/
    public List<ApiKeyView> getAllKeys(){
        return apiKeyRepository.findAll()
                .stream()
                .map(e -> ApiKeyView.builder()
                        .id(e.getId())
                        .keyId(e.getKeyId())
                        .clientName(e.getClientName())
                        .role(e.getRole())
                        .active(e.isActive())
                        .createdAt(e.getCreatedAt())
                        .lastUsedAt(e.getLastUsedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
