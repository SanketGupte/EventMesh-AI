package com.eventmesh.routing.repository;

import com.eventmesh.common.dto.RoutingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutingRuleRepository extends JpaRepository<RoutingRule, Long> {
    Optional<RoutingRule> findByEventType(String eventType);
}
