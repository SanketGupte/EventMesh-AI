package com.eventmesh.ingestion.auth.controller;

import com.eventmesh.common.ApiResponse;
import com.eventmesh.ingestion.auth.dto.ApiKeyCreateResponse;
import com.eventmesh.ingestion.auth.dto.ApiKeyView;
import com.eventmesh.ingestion.auth.exception.CustomAuthException;
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
public class  ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * ADMIN ONLY
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ApiKeyCreateResponse>> createApiKey(
            @RequestParam String clientName,
            @RequestParam String role,
            HttpServletRequest request) throws CustomAuthException {

        AutorizationUtil.requireAdmin(request);

        ApiKeyCreateResponse apiKey = apiKeyService.generateApiKey(clientName, role);

        return ResponseEntity.ok(
                ApiResponse.success("API Key created successfully", apiKey)
        );
    }

    /**
     * ADMIN ONLY
     */
    @DeleteMapping("/{keyId}")
    public ResponseEntity<ApiResponse<String>> deactivateApiKey(
            @PathVariable String keyId,
            HttpServletRequest request) throws CustomAuthException {

        AutorizationUtil.requireAdmin(request);

        apiKeyService.deactivateKeyById(keyId);

        return ResponseEntity.ok(
                ApiResponse.success("API Key deactivated successfully")
        );
    }

    /**
     * ADMIN / VIEWER
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKeyView>>> getAllKeys(
            HttpServletRequest request) throws CustomAuthException {

        AutorizationUtil.requireAdminOrViewer(request);

        List<ApiKeyView> keys = apiKeyService.getAllKeys();

        return ResponseEntity.ok(
                ApiResponse.success("API Keys fetched successfully", keys)
        );
    }
}
