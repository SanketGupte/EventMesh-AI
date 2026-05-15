//package com.eventmesh.routing.repository;
//
//import com.eventmesh.routing.entity.RoutingRuleEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit Tests for RoutingRuleRepository.
// * Tests JPA repository operations for routing rules.
// */
//@DataJpaTest
//@ActiveProfiles("test")
//class RoutingRuleRepositoryTest {
//
//    @Autowired
//    private RoutingRuleRepository routingRuleRepository;
//
//    private RoutingRuleEntity testRule;
//
//    @BeforeEach
//    void setUp() {
//        testRule = RoutingRuleEntity.builder()
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
//    void testSaveRoutingRule() {
//        // Act
//        RoutingRuleEntity saved = routingRuleRepository.save(testRule);
//
//        // Assert
//        assertNotNull(saved.getId());
//        assertEquals("ORDER_CREATED", saved.getEventType());
//    }
//
//    @Test
//    void testFindByEventType() {
//        // Arrange
//        routingRuleRepository.save(testRule);
//
//        // Act
//        List<RoutingRuleEntity> rules = routingRuleRepository.findByEventType("ORDER_CREATED");
//
//        // Assert
//        assertFalse(rules.isEmpty());
//        assertTrue(rules.stream().allMatch(r -> r.getEventType().equals("ORDER_CREATED")));
//    }
//
//    @Test
//    void testFindByActiveTrue() {
//        // Arrange
//        routingRuleRepository.save(testRule);
//
//        // Act
//        List<RoutingRuleEntity> rules = routingRuleRepository.findByActiveTrue();
//
//        // Assert
//        assertFalse(rules.isEmpty());
//        assertTrue(rules.stream().allMatch(RoutingRuleEntity::getActive));
//    }
//
//    @Test
//    void testFindByActiveFalse() {
//        // Arrange
//        testRule.setActive(false);
//        routingRuleRepository.save(testRule);
//
//        // Act
//        List<RoutingRuleEntity> rules = routingRuleRepository.findByActiveTrue();
//
//        // Assert - Rule should not be in active list
//        assertTrue(rules.stream().noneMatch(r -> r.getEventType().equals("ORDER_CREATED")));
//    }
//
//    @Test
//    void testDeleteByEventType() {
//        // Arrange
//        routingRuleRepository.save(testRule);
//
//        // Act
//        routingRuleRepository.deleteByEventType("ORDER_CREATED");
//
//        // Assert
//        List<RoutingRuleEntity> rules = routingRuleRepository.findByEventType("ORDER_CREATED");
//        assertTrue(rules.isEmpty());
//    }
//}
//
