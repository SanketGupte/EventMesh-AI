package com.eventmesh.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class EventDTO {
    @NotBlank(message = "eventId is required")
    private String eventId;

    @NotBlank(message = "eventType is required")
    private String eventType;

    @NotBlank(message = "source is required")
    private String source;

    @NotNull(message = "timestamp is required")
    private Instant timestamp;

    @NotNull(message = "payload cannot be null")
    private Map<String, Object> payload;

    private Map<String,String> metadata;
}
