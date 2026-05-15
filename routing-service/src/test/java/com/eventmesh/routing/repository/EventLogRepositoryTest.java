//package com.eventmesh.routing.repository;
//
//import com.eventmesh.routing.entity.EventLog;
//import com.eventmesh.routing.enums.EventStatus;
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
// * Unit Tests for EventLogRepository.
// * Tests JPA repository operations for event logs.
// */
//@DataJpaTest
//@ActiveProfiles("test")
//class EventLogRepositoryTest {
//
//    @Autowired
//    private EventLogRepository eventLogRepository;
//
//    private EventLog testEventLog;
//
//    @BeforeEach
//    void setUp() {
//        testEventLog = EventLog.builder()
//            .eventId("evt-test-001")
//            .eventType("ORDER_CREATED")
//            .source("order-service")
//            .status(EventStatus.PENDING)
//            .destination("warehouse-service")
//            .retryCount(0)
//            .correlationId("corr-123")
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//    }
//
//    @Test
//    void testSaveEventLog() {
//        // Act
//        EventLog savedLog = eventLogRepository.save(testEventLog);
//
//        // Assert
//        assertNotNull(savedLog.getId());
//        assertEquals("evt-test-001", savedLog.getEventId());
//    }
//
//    @Test
//    void testFindByEventId() {
//        // Arrange
//        eventLogRepository.save(testEventLog);
//
//        // Act
//        Optional<EventLog> foundLog = eventLogRepository.findByEventId("evt-test-001");
//
//        // Assert
//        assertTrue(foundLog.isPresent());
//        assertEquals("ORDER_CREATED", foundLog.get().getEventType());
//    }
//
//    @Test
//    void testFindByEventIdNotFound() {
//        // Act
//        Optional<EventLog> foundLog = eventLogRepository.findByEventId("non-existent");
//
//        // Assert
//        assertFalse(foundLog.isPresent());
//    }
//
//    @Test
//    void testFindByStatus() {
//        // Arrange
//        eventLogRepository.save(testEventLog);
//
//        // Act
//        List<EventLog> logs = eventLogRepository.findByStatus(EventStatus.PENDING);
//
//        // Assert
//        assertFalse(logs.isEmpty());
//        assertTrue(logs.stream().allMatch(log -> log.getStatus() == EventStatus.PENDING));
//    }
//
//    @Test
//    void testFindByEventType() {
//        // Arrange
//        eventLogRepository.save(testEventLog);
//
//        // Act
//        List<EventLog> logs = eventLogRepository.findByEventType("ORDER_CREATED");
//
//        // Assert
//        assertFalse(logs.isEmpty());
//        assertTrue(logs.stream().allMatch(log -> log.getEventType().equals("ORDER_CREATED")));
//    }
//
//    @Test
//    void testExistsByEventId() {
//        // Arrange
//        eventLogRepository.save(testEventLog);
//
//        // Act
//        boolean exists = eventLogRepository.existsByEventId("evt-test-001");
//
//        // Assert
//        assertTrue(exists);
//    }
//}
//
