package com.eventmesh.ingestion.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyCreateResponse {
    private String keyId;
    private String apiKey; // plaintext returned only once
}

