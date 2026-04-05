package com.eventmesh.routing.service;

import com.eventmesh.common.dto.RoutingRule;
import com.eventmesh.routing.entity.RoutingRuleEntity;
import com.eventmesh.routing.repository.RoutingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutingRuleService {

    private final RoutingRuleRepository routingRuleRepository;

//    public RoutingRuleService() {
//        RoutingRuleEntity entity = new RoutingRuleEntity();
//        entity.setEventType("ORDER_CREATED");
//        orderRule.setDestinationTopic("event.route.payment");
//
//        rules.add(orderRule);
//    }

    public String getDestinationTopic(String eventType) {
//        for(RoutingRule rule: rules){
//            if(rule.getEventType().equals(eventType)){
//                return rule.getDestinationTopic();
//            }
//        }
        return routingRuleRepository.findByEventType(eventType).map(RoutingRule::getDestinationTopic).orElse("event.route.default");
    }

    //Add new Rule
    public void addRule(RoutingRule rule){
        RoutingRuleEntity entity = new RoutingRuleEntity();
        entity.setEventType(rule.getEventType());
        entity.setDestinationTopic(rule.getDestinationTopic());
        routingRuleRepository.save(entity);
    }

    //Get all Rules
    public List<RoutingRule> getAllRules(){
        return routingRuleRepository.findAll().stream()
                .map(entity -> {
                    RoutingRule dto = new RoutingRule();
                    dto.setEventType(entity.getEventType());;
                    dto.setDestinationTopic(entity.getDestinationTopic());
                    return dto;
                }).collect(Collectors.toList());
    }

}
