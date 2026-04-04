package com.eventmesh.common.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "routing_rules")
@Data
public class RoutingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String eventType;

    private String destinationTopic;
}
