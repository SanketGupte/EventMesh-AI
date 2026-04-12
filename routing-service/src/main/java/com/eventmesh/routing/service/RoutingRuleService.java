package com.eventmesh.routing.service;

import com.eventmesh.common.dto.RoutingRule;
import com.eventmesh.routing.entity.RoutingRuleEntity;
import com.eventmesh.routing.repository.RoutingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingRuleService {

    private final RoutingRuleRepository routingRuleRepository;

    public String getDestinationTopic(String eventType) {
        return routingRuleRepository.findByEventType(eventType)
                .map(RoutingRuleEntity::getDestinationTopic)
                .orElseThrow(()-> new IllegalArgumentException("No routing rule found for event type: " + eventType));
    }

    //Add new Rule
    public RoutingRuleEntity addRule(RoutingRule rule){
        routingRuleRepository.findByEventType(rule.getEventType())
                .ifPresent(existingRule -> {
            throw new IllegalArgumentException("Rule already exists for event type: " + rule.getEventType());
        });

        RoutingRuleEntity entity = new RoutingRuleEntity();
        entity.setEventType(rule.getEventType());
        entity.setDestinationTopic(rule.getDestinationTopic());
        routingRuleRepository.save(entity);
        return entity;
    }

    //Get all Rules
    public List<RoutingRule> getAllRules(){
        return routingRuleRepository.findAll().stream()
                .map(entity -> {
                    RoutingRule dto = new RoutingRule();
                    dto.setEventType(entity.getEventType());;
                    dto.setDestinationTopic(entity.getDestinationTopic());
                    return dto;
                }).toList();
    }

    @Transactional
    public void deleteById(Long id){
        if(!routingRuleRepository.existsById(id)){
            throw new IllegalArgumentException("Rule not found for id: " + id);
        }
        routingRuleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByEventType(String eventType){
        int deleteCount = routingRuleRepository.deleteByEventType(eventType);
        if(deleteCount == 0){
            throw new IllegalArgumentException("No Rule found for EventType: " + eventType);
        }
    }

}
