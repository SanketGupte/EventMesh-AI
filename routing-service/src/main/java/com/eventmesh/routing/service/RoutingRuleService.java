package com.eventmesh.routing.service;

import com.eventmesh.common.dto.RoutingRule;
import com.eventmesh.routing.repository.RoutingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingRuleService {

    private final RoutingRuleRepository routingRuleRepository;

//    public RoutingRuleService() {
//        RoutingRule orderRule = new RoutingRule();
//        orderRule.setEventType("ORDER_CREATED");
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
        routingRuleRepository.save(rule);
    }

    //Get all Rules
    public List<RoutingRule> getAllRules(){
        return routingRuleRepository.findAll();
    }

}
