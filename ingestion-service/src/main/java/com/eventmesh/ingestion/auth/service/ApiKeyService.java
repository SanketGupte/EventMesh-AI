package com.eventmesh.ingestion.auth.service;

import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import com.eventmesh.ingestion.auth.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    /*Validate API Key (CORE AUTH LOGIC)*/
    public ApiKeyEntity validateApiKey(String apiKey) {
        ApiKeyEntity entity = apiKeyRepository.findByApiKey(apiKey)
                .orElseThrow(() -> {
                    log.warn("Invalid api key {}", apiKey);
                    return new RuntimeException("Invalid Api Key");
                });

        if (!entity.isActive()){
            log.warn("Inactive api key used: {}", apiKey);
            throw new RuntimeException("API Key is inactive.");
        }

        //Optional: track usage
        entity.setLastUsedAt(LocalDateTime.now());
        apiKeyRepository.save(entity);

        return entity;
    }

    /*Generate new API Key*/
    public ApiKeyEntity generateApiKey(String clientName, String role){
        String generatedKey = UUID.randomUUID().toString();
        ApiKeyEntity entity = ApiKeyEntity.builder()
                .apiKey(generatedKey)
                .clientName(clientName)
                .role(role)
                .active(true)
                .build();
        try{
            ApiKeyEntity saved = apiKeyRepository.save(entity);
            log.info("Api Key generated for {} with role {}", clientName, role);
            return saved;
        } catch (DataIntegrityViolationException ex){
            log.error("API Key generation failed due to duplication");
            throw  new RuntimeException("Failed to generate API Key");
        }
    }

    /*Deactivate API Key*/
    public void deactivateKey(String apiKey){
        ApiKeyEntity entity = apiKeyRepository.findByApiKey(apiKey).orElseThrow(() -> new RuntimeException("API Key not found"));

        entity.setActive(false);
        apiKeyRepository.save(entity);
        log.info("API Key deactivated: {}", apiKey);
    }

    /*Fetch All API Keys*/
    public List<ApiKeyEntity> getAllKeys(){
        return apiKeyRepository.findAll();
    }
}
