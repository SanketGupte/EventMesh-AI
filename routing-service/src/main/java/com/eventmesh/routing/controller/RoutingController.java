package com.eventmesh.routing.controller;

import com.eventmesh.common.dto.RoutingRule;
import com.eventmesh.routing.service.RoutingRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routing-rules")
@RequiredArgsConstructor
public class RoutingController {
    private final RoutingRuleService routingRuleService;

    //Function to add new Rule
    @PostMapping
    public String addRule(@RequestBody @Valid RoutingRule rule){
        routingRuleService.addRule(rule);
        return "Rule added successfully";
    }

    //Get all Rules
    @GetMapping
    public List<RoutingRule> getAllRules(){
        return routingRuleService.getAllRules();
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id){
        routingRuleService.deleteById(id);
        return "Rule deleted by ID";
    }

    @DeleteMapping("/event/{eventType}")
    public String deleteByEventType(@PathVariable("eventType") String eventType){
        routingRuleService.deleteByEventType(eventType);
        return "Rule deleted by Event Type";
    }

}
