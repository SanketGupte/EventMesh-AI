//package com.eventmesh.routing.service;
//
//import com.eventmesh.routing.entity.EventLog;
//import com.eventmesh.routing.enums.EventStatus;
//import com.eventmesh.routing.repository.EventLogRepository;
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
// * Unit Tests for EventLogService.
// * Tests event logging and retrieval functionality.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class EventLogServiceTest {
//
//    @Mock
//    private EventLogRepository eventLogRepository;
//
//    @InjectMocks
//    private EventLogService eventLogService;
//
//    private EventLog testEventLog;
//
//    @BeforeEach
//    void setUp() {
//        testEventLog = EventLog.builder()
//            .id(1L)
//            .eventId("evt-001")
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
//        // Arrange
//        when(eventLogRepository.save(any(EventLog.class)))
//            .thenReturn(testEventLog);
//
//        // Act
//        EventLog result = eventLogService.saveEventLog(testEventLog);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("evt-001", result.getEventId());
//        assertEquals(EventStatus.PENDING, result.getStatus());
//        verify(eventLogRepository, times(1)).save(testEventLog);
//    }
//
//    @Test
//    void testGetEventLogById() {
//        // Arrange
//        when(eventLogRepository.findById(1L))
//            .thenReturn(Optional.of(testEventLog));
//
//        // Act
//        Optional<EventLog> result = eventLogService.getEventLogById(1L);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals("evt-001", result.get().getEventId());
//    }
//
//    @Test
//    void testGetEventLogByIdNotFound() {
//        // Arrange
//        when(eventLogRepository.findById(999L))
//            .thenReturn(Optional.empty());
//
//        // Act
//        Optional<EventLog> result = eventLogService.getEventLogById(999L);
//
//        // Assert
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void testGetEventLogsByStatus() {
//        // Arrange
//        List<EventLog> logs = List.of(testEventLog);
//        when(eventLogRepository.findByStatus(EventStatus.PENDING))
//            .thenReturn(logs);
//
//        // Act
//        List<EventLog> result = eventLogService.getEventLogsByStatus(EventStatus.PENDING);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(EventStatus.PENDING, result.get(0).getStatus());
//    }
//
//    @Test
//    void testGetEventLogsByEventType() {
//        // Arrange
//        List<EventLog> logs = List.of(testEventLog);
//        when(eventLogRepository.findByEventType("ORDER_CREATED"))
//            .thenReturn(logs);
//
//        // Act
//        List<EventLog> result = eventLogService.getEventLogsByEventType("ORDER_CREATED");
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("ORDER_CREATED", result.get(0).getEventType());
//    }
//
//    @Test
//    void testUpdateEventStatus() {
//        // Arrange
//        testEventLog.setStatus(EventStatus.DELIVERED);
//        when(eventLogRepository.save(any(EventLog.class)))
//            .thenReturn(testEventLog);
//
//        // Act
//        EventLog result = eventLogService.saveEventLog(testEventLog);
//
//        // Assert
//        assertEquals(EventStatus.DELIVERED, result.getStatus());
//    }
//}
//
