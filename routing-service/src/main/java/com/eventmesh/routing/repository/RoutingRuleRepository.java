package com.eventmesh.routing.repository;

import com.eventmesh.routing.entity.RoutingRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutingRuleRepository extends JpaRepository<RoutingRuleEntity, Long> {
    Optional<RoutingRuleEntity> findByEventType(String eventType);

    int deleteByEventType(String eventType);
}
