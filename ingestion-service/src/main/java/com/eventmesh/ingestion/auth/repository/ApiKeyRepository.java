package com.eventmesh.ingestion.auth.repository;

import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    /*Find API Key by value*/
    Optional<ApiKeyEntity>  findByApiKey(String apiKey);
    /*Check if API Key exists*/
    boolean existsByApiKey(String apiKey);
}
