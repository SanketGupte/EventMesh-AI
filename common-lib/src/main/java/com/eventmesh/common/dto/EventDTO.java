package com.eventmesh.common.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EventDTO {
    private String eventId;
    private String eventType;
    private String source;
    private String timestamp;

    private Map<String, Object> payload;
    private Map<String,String> metadata;
}
