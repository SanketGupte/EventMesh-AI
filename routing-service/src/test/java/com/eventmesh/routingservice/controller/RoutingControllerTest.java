//package com.eventmesh.routing.controller;
//
//import com.eventmesh.common.ApiResponse;
//import com.eventmesh.routing.entity.RoutingRuleEntity;
//import com.eventmesh.routing.service.RoutingRuleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * Unit Tests for RoutingController.
// * Tests routing rule REST API endpoints.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class RoutingControllerTest {
//
//    @Mock
//    private RoutingRuleService routingRuleService;
//
//    @InjectMocks
//    private RoutingController routingController;
//
//    private RoutingRuleEntity testRule;
//
//    @BeforeEach
//    void setUp() {
//        testRule = RoutingRuleEntity.builder()
//            .id(1L)
//            .eventType("ORDER_CREATED")
//            .destination("warehouse-service")
//            .active(true)
//            .priority(1)
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//    }
//
//    @Test
//    void testCreateRoutingRule() {
//        // Arrange
//        when(routingRuleService.createRoutingRule(any(RoutingRuleEntity.class)))
//            .thenReturn(testRule);
//
//        // Act
//        ResponseEntity<ApiResponse> response = routingController.createRoutingRule(testRule);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(routingRuleService, times(1)).createRoutingRule(any());
//    }
//
//    @Test
//    void testGetAllRoutingRules() {
//        // Arrange
//        List<RoutingRuleEntity> rules = List.of(testRule);
//        when(routingRuleService.getAllRoutingRules()).thenReturn(rules);
//
//        // Act
//        ResponseEntity<ApiResponse> response = routingController.getAllRoutingRules();
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(routingRuleService, times(1)).getAllRoutingRules();
//    }
//
//    @Test
//    void testDeleteRoutingRuleById() {
//        // Arrange
//        doNothing().when(routingRuleService).deleteRoutingRule(1L);
//
//        // Act
//        ResponseEntity<ApiResponse> response = routingController.deleteRoutingRule(1L);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(routingRuleService, times(1)).deleteRoutingRule(1L);
//    }
//
//    @Test
//    void testDeleteRoutingRulesByEventType() {
//        // Arrange
//        doNothing().when(routingRuleService).deleteRoutingRulesByEventType("ORDER_CREATED");
//
//        // Act
//        ResponseEntity<ApiResponse> response = routingController.deleteRoutingRulesByEventType("ORDER_CREATED");
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(routingRuleService, times(1)).deleteRoutingRulesByEventType("ORDER_CREATED");
//    }
//}
//
