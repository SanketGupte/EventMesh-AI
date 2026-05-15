package com.eventmesh.ingestion.auth.repository;

import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

 
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    /*Find API Key by public keyId*/
    Optional<ApiKeyEntity> findByKeyId(String keyId);
    /*Check if keyId exists*/
    boolean existsByKeyId(String keyId);
}
