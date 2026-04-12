package com.eventmesh.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoutingRule {

    @NotBlank
    private String eventType;

    @NotBlank
    private String destinationTopic;
}
