package com.eventmesh.ingestion.auth.controller;

import com.eventmesh.common.ApiResponse;
import com.eventmesh.ingestion.auth.entity.ApiKeyEntity;
import com.eventmesh.ingestion.auth.service.ApiKeyService;
import com.eventmesh.ingestion.auth.utils.AutorizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * ADMIN ONLY
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ApiKeyEntity>> createApiKey(
            @RequestParam String clientName,
            @RequestParam String role,
            HttpServletRequest request) {

        AutorizationUtil.requireAdmin(request);

        ApiKeyEntity apiKey = apiKeyService.generateApiKey(clientName, role);

        return ResponseEntity.ok(
                ApiResponse.success("API Key created successfully", apiKey)
        );
    }

    /**
     * ADMIN ONLY
     */
    @DeleteMapping("/{apiKey}")
    public ResponseEntity<ApiResponse<String>> deactivateApiKey(
            @PathVariable String apiKey,
            HttpServletRequest request) {

        AutorizationUtil.requireAdmin(request);

        apiKeyService.deactivateKey(apiKey);

        return ResponseEntity.ok(
                ApiResponse.success("API Key deactivated successfully")
        );
    }

    /**
     * ADMIN / VIEWER
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKeyEntity>>> getAllKeys(
            HttpServletRequest request) {

        AutorizationUtil.requireAdmin(request);

        List<ApiKeyEntity> keys = apiKeyService.getAllKeys();

        return ResponseEntity.ok(
                ApiResponse.success("API Keys fetched successfully", keys)
        );
    }
}
