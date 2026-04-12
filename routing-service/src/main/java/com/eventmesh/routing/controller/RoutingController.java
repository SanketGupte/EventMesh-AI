package com.eventmesh.routing.controller;

import com.eventmesh.common.ApiResponse;
import com.eventmesh.common.dto.RoutingRule;
import com.eventmesh.routing.entity.RoutingRuleEntity;
import com.eventmesh.routing.service.RoutingRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routing-rules")
@RequiredArgsConstructor
public class RoutingController {
    private final RoutingRuleService routingRuleService;

    //Function to add new Rule
    @PostMapping
    public ResponseEntity<ApiResponse<RoutingRuleEntity>> addRule(@RequestBody @Valid RoutingRule rule){
        RoutingRuleEntity addedRule = routingRuleService.addRule(rule);
        return  ResponseEntity.ok(ApiResponse.success("Routing rule added successfully", addedRule));
    }

    //Get all Rules
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutingRule>>>  getAllRules(){
        List<RoutingRule> rules = routingRuleService.getAllRules();
        return ResponseEntity.ok(ApiResponse.success("Routing rules fetched successfully", rules));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable("id") Long id){
        routingRuleService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Routing rule deleted successfully."));
    }

    @DeleteMapping("/event/{eventType}")
    public ResponseEntity<ApiResponse<String>> deleteByEventType(@PathVariable("eventType") String eventType){
        routingRuleService.deleteByEventType(eventType);
        return ResponseEntity.ok(ApiResponse.success("Routing rule deleted successfully."));
    }

}
