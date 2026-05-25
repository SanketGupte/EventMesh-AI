//package com.eventmesh.routing.controller;
//
//import com.eventmesh.common.ApiResponse;
//import com.eventmesh.routing.entity.EventLog;
//import com.eventmesh.routing.enums.EventStatus;
//import com.eventmesh.routing.service.EventLogService;
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
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit Tests for EventLogController.
// * Tests event log retrieval REST API endpoints.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class EventLogControllerTest {
//
//    @Mock
//    private EventLogService eventLogService;
//
//    @InjectMocks
//    private EventLogController eventLogController;
//
//    private EventLog testEventLog;
//
//    @BeforeEach
//    void setUp() {
//        testEventLog = EventLog.builder()
//            .id(1L)
//            .eventId("evt-001")
//            .eventType("ORDER_CREATED")
//            .status(EventStatus.DELIVERED)
//            .destination("warehouse-service")
//            .retryCount(0)
//            .createdAt(LocalDateTime.now())
//            .processedAt(LocalDateTime.now())
//            .build();
//    }
//
//    @Test
//    void testGetAllEventLogs() {
//        // Arrange
//        List<EventLog> logs = List.of(testEventLog);
//        when(eventLogService.getAllEventLogs()).thenReturn(logs);
//
//        // Act
//        ResponseEntity<ApiResponse> response = eventLogController.getAllEventLogs();
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(eventLogService, times(1)).getAllEventLogs();
//    }
//
//    @Test
//    void testGetEventLogsByStatus() {
//        // Arrange
//        List<EventLog> logs = List.of(testEventLog);
//        when(eventLogService.getEventLogsByStatus(EventStatus.DELIVERED))
//            .thenReturn(logs);
//
//        // Act
//        ResponseEntity<ApiResponse> response = eventLogController.getEventLogsByStatus("DELIVERED");
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(eventLogService, times(1)).getEventLogsByStatus(EventStatus.DELIVERED);
//    }
//
//    @Test
//    void testGetEventLogsByEventType() {
//        // Arrange
//        List<EventLog> logs = List.of(testEventLog);
//        when(eventLogService.getEventLogsByEventType("ORDER_CREATED"))
//            .thenReturn(logs);
//
//        // Act
//        ResponseEntity<ApiResponse> response = eventLogController.getEventLogsByEventType("ORDER_CREATED");
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(eventLogService, times(1)).getEventLogsByEventType("ORDER_CREATED");
//    }
//
//    @Test
//    void testGetEventLogsByStatusWithInvalidStatus() {
//        // Act & Assert
//        assertThrows(Exception.class, () -> {
//            eventLogController.getEventLogsByStatus("INVALID_STATUS");
//        });
//    }
//}
//
