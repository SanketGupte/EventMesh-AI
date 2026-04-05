package com.eventmesh.common.dto;

import lombok.Data;

@Data
public class RoutingRule {

    private String eventType;

    private String destinationTopic;
}
