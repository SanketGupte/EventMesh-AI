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

    @Column(nullable = false, unique = true)
    private String eventType;

    @Column(nullable = false)
    private String destinationTopic;

}
