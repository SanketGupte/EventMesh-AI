//package com.eventmesh.routing.service;
//
//import com.eventmesh.routing.entity.RoutingRuleEntity;
//import com.eventmesh.routing.repository.RoutingRuleRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
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
// * Unit Tests for RoutingRuleService.
// * Tests routing rule CRUD operations and management.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class RoutingRuleServiceTest {
//
//    @Mock
//    private RoutingRuleRepository routingRuleRepository;
//
//    @InjectMocks
//    private RoutingRuleService routingRuleService;
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
//            .description("Route order events to warehouse")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//    }
//
//    @Test
//    void testCreateRoutingRule() {
//        // Arrange
//        when(routingRuleRepository.save(any(RoutingRuleEntity.class)))
//            .thenReturn(testRule);
//
//        // Act
//        RoutingRuleEntity result = routingRuleService.createRoutingRule(testRule);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("ORDER_CREATED", result.getEventType());
//        assertEquals("warehouse-service", result.getDestination());
//        verify(routingRuleRepository, times(1)).save(testRule);
//    }
//
//    @Test
//    void testGetRoutingRuleById() {
//        // Arrange
//        when(routingRuleRepository.findById(1L))
//            .thenReturn(Optional.of(testRule));
//
//        // Act
//        Optional<RoutingRuleEntity> result = routingRuleService.getRoutingRuleById(1L);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals("ORDER_CREATED", result.get().getEventType());
//    }
//
//    @Test
//    void testUpdateRoutingRule() {
//        // Arrange
//        testRule.setDestination("new-destination");
//        when(routingRuleRepository.save(any(RoutingRuleEntity.class)))
//            .thenReturn(testRule);
//
//        // Act
//        RoutingRuleEntity result = routingRuleService.updateRoutingRule(testRule);
//
//        // Assert
//        assertEquals("new-destination", result.getDestination());
//    }
//
//    @Test
//    void testDeleteRoutingRule() {
//        // Arrange
//        doNothing().when(routingRuleRepository).deleteById(1L);
//
//        // Act
//        routingRuleService.deleteRoutingRule(1L);
//
//        // Assert
//        verify(routingRuleRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    void testGetAllRoutingRules() {
//        // Arrange
//        List<RoutingRuleEntity> rules = List.of(testRule);
//        when(routingRuleRepository.findAll()).thenReturn(rules);
//
//        // Act
//        List<RoutingRuleEntity> result = routingRuleService.getAllRoutingRules();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetActiveRoutingRules() {
//        // Arrange
//        List<RoutingRuleEntity> rules = List.of(testRule);
//        when(routingRuleRepository.findByActiveTrue()).thenReturn(rules);
//
//        // Act
//        List<RoutingRuleEntity> result = routingRuleService.getActiveRoutingRules();
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.stream().allMatch(RoutingRuleEntity::getActive));
//    }
//
//    @Test
//    void testGetRoutingRulesByEventType() {
//        // Arrange
//        List<RoutingRuleEntity> rules = List.of(testRule);
//        when(routingRuleRepository.findByEventType("ORDER_CREATED"))
//            .thenReturn(rules);
//
//        // Act
//        List<RoutingRuleEntity> result = routingRuleService.getRoutingRulesByEventType("ORDER_CREATED");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("ORDER_CREATED", result.get(0).getEventType());
//    }
//}
//
