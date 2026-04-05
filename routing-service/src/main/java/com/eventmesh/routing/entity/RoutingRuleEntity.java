package com.eventmesh.routing.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "routing_rules")
@Data
public class RoutingRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;
    private String destinationTopic;

}
