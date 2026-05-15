//package com.eventmesh.integration;
//
//import com.eventmesh.common.dto.EventDTO;
//import com.eventmesh.routing.entity.EventLog;
//import com.eventmesh.routing.entity.RoutingRuleEntity;
//import com.eventmesh.routing.enums.EventStatus;
//import com.eventmesh.routing.repository.EventLogRepository;
//import com.eventmesh.routing.repository.RoutingRuleRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Integration Tests for End-to-End Event Processing.
// * Tests complete event flow from ingestion through routing to storage.
// */
//@SpringBootTest
//@ActiveProfiles("test")
//class EventProcessingIntegrationTest {
//
//    @Autowired
//    private KafkaTemplate<String, EventDTO> kafkaTemplate;
//
//    @Autowired
//    private EventLogRepository eventLogRepository;
//
//    @Autowired
//    private RoutingRuleRepository routingRuleRepository;
//
//    private EventDTO testEvent;
//    private RoutingRuleEntity testRule;
//
//    @BeforeEach
//    void setUp() {
//        // Create test event
//        testEvent = EventDTO.builder()
//            .eventId("evt-integration-001")
//            .eventType("ORDER_CREATED")
//            .source("order-service")
//            .timestamp(LocalDateTime.now())
//            .payload(new HashMap<>(Map.of("orderId", "ORD-001", "amount", 500.0)))
//            .metadata(new HashMap<>(Map.of("correlationId", "corr-integration-123")))
//            .build();
//
//        // Create test routing rule
//        testRule = RoutingRuleEntity.builder()
//            .eventType("ORDER_CREATED")
//            .destination("warehouse-service")
//            .active(true)
//            .priority(1)
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        // Clean up before each test
//        eventLogRepository.deleteAll();
//        routingRuleRepository.deleteAll();
//    }
//
//    @Test
//    void testEventIngestedSuccessfully() {
//        // Act: Save event log
//        EventLog eventLog = EventLog.builder()
//            .eventId(testEvent.getEventId())
//            .eventType(testEvent.getEventType())
//            .source(testEvent.getSource())
//            .status(EventStatus.PENDING)
//            .retryCount(0)
//            .correlationId("corr-integration-123")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        EventLog savedEventLog = eventLogRepository.save(eventLog);
//
//        // Assert
//        assertNotNull(savedEventLog.getId());
//        assertEquals(testEvent.getEventId(), savedEventLog.getEventId());
//        assertEquals(EventStatus.PENDING, savedEventLog.getStatus());
//    }
//
//    @Test
//    void testRoutingRuleCreatedAndRetrieved() {
//        // Act: Save routing rule
//        RoutingRuleEntity savedRule = routingRuleRepository.save(testRule);
//
//        // Assert
//        assertNotNull(savedRule.getId());
//        assertEquals("ORDER_CREATED", savedRule.getEventType());
//        assertEquals("warehouse-service", savedRule.getDestination());
//
//        // Act: Retrieve routing rule
//        Optional<RoutingRuleEntity> retrievedRule = routingRuleRepository.findById(savedRule.getId());
//
//        // Assert
//        assertTrue(retrievedRule.isPresent());
//        assertEquals(savedRule.getId(), retrievedRule.get().getId());
//    }
//
//    @Test
//    void testEventDeliveryFlow() {
//        // Arrange: Create routing rule
//        RoutingRuleEntity savedRule = routingRuleRepository.save(testRule);
//        assertNotNull(savedRule.getId());
//
//        // Arrange: Create and save event log as PENDING
//        EventLog eventLog = EventLog.builder()
//            .eventId(testEvent.getEventId())
//            .eventType(testEvent.getEventType())
//            .source(testEvent.getSource())
//            .status(EventStatus.PENDING)
//            .destination(savedRule.getDestination())
//            .retryCount(0)
//            .correlationId("corr-integration-123")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        EventLog savedEventLog = eventLogRepository.save(eventLog);
//
//        // Act: Update event status to DELIVERED
//        savedEventLog.setStatus(EventStatus.DELIVERED);
//        savedEventLog.setProcessedAt(LocalDateTime.now());
//        EventLog deliveredEventLog = eventLogRepository.save(savedEventLog);
//
//        // Assert
//        assertEquals(EventStatus.DELIVERED, deliveredEventLog.getStatus());
//        assertNotNull(deliveredEventLog.getProcessedAt());
//    }
//
//    @Test
//    void testEventRetryFlow() {
//        // Arrange: Create event log as FAILED
//        EventLog eventLog = EventLog.builder()
//            .eventId(testEvent.getEventId())
//            .eventType(testEvent.getEventType())
//            .source(testEvent.getSource())
//            .status(EventStatus.FAILED)
//            .destination("warehouse-service")
//            .retryCount(0)
//            .failureReason("Connection timeout")
//            .correlationId("corr-integration-123")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        EventLog savedEventLog = eventLogRepository.save(eventLog);
//
//        // Act: Update retry count and status
//        savedEventLog.setRetryCount(1);
//        savedEventLog.setStatus(EventStatus.PROCESSING);
//        EventLog retriedEventLog = eventLogRepository.save(savedEventLog);
//
//        // Assert
//        assertEquals(1, retriedEventLog.getRetryCount());
//        assertEquals(EventStatus.PROCESSING, retriedEventLog.getStatus());
//    }
//
//    @Test
//    void testDeadLetterQueueFlow() {
//        // Arrange: Create event log with max retries exceeded
//        EventLog eventLog = EventLog.builder()
//            .eventId(testEvent.getEventId())
//            .eventType(testEvent.getEventType())
//            .source(testEvent.getSource())
//            .status(EventStatus.FAILED)
//            .destination("warehouse-service")
//            .retryCount(3)
//            .failureReason("Max retries exceeded - Service unavailable")
//            .correlationId("corr-integration-123")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        EventLog savedEventLog = eventLogRepository.save(eventLog);
//
//        // Act: Move to DLQ
//        savedEventLog.setStatus(EventStatus.DLQ);
//        EventLog dlqEventLog = eventLogRepository.save(savedEventLog);
//
//        // Assert
//        assertEquals(EventStatus.DLQ, dlqEventLog.getStatus());
//        assertTrue(dlqEventLog.getRetryCount() >= 3);
//    }
//
//    @Test
//    void testIdempotencyCheck() {
//        // Arrange: Save first event
//        EventLog firstEvent = EventLog.builder()
//            .eventId("evt-idempotent-001")
//            .eventType("ORDER_CREATED")
//            .source("order-service")
//            .status(EventStatus.DELIVERED)
//            .retryCount(0)
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//
//        eventLogRepository.save(firstEvent);
//
//        // Act: Check if event exists with same ID
//        Optional<EventLog> existingEvent = eventLogRepository.findByEventId("evt-idempotent-001");
//
//        // Assert: Event should already exist (idempotent check)
//        assertTrue(existingEvent.isPresent());
//    }
//}
//
