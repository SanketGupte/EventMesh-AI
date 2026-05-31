//package com.eventmesh.ingestionservice.controller;
//
//import com.eventmesh.common.ApiResponse;
//import com.eventmesh.common.dto.EventDTO;
//import com.eventmesh.ingestion.producer.EventProducer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import com.eventmesh.ingestion.controller.EventController;
//
///**
// * Unit Tests for EventController.
// * Tests event publishing REST endpoint functionality.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class EventControllerTest {
//
//    @Mock
//    private EventProducer eventProducer;
//
//    @InjectMocks
//    private EventController eventController;
//
//    private EventDTO validEvent;
//
//    @BeforeEach
//    void setUp() {
//        validEvent = EventDTO.builder()
//            .eventId("evt-001")
//            .eventType("ORDER_CREATED")
//            .source("order-service")
//            .timestamp(Instant.from(Instant.now()))
//            .payload(new HashMap<>(Map.of("orderId", "ORD-001", "amount", 100.0)))
//            .metadata(new HashMap<>(Map.of("correlationId", "corr-123")))
//            .build();
//    }
//
//    @Test
//    void testPublishEventSuccess() {
//        // Arrange
//        doNothing().when(eventProducer).send(any(EventDTO.class));
//
//        // Act
//        ResponseEntity<ApiResponse<EventDTO>> response = eventController.publishEvent(validEvent);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//        verify(eventProducer, times(1)).send(validEvent);
//    }
//
//    @Test
//    void testPublishEventWithNullEventId() {
//        // Arrange
//        validEvent.setEventId(null);
//
//        // Act & Assert
//        assertThrows(Exception.class, () -> eventController.publishEvent(validEvent));
//        verify(eventProducer, never()).send(any());
//    }
//
//    @Test
//    void testPublishEventWithInvalidEventType() {
//        // Arrange
//        validEvent.setEventType("");
//
//        // Act & Assert
//        assertThrows(Exception.class, () -> eventController.publishEvent(validEvent));
//        verify(eventProducer, never()).send(any());
//    }
//
//    @Test
//    void testPublishEventProducerFailure() {
//        // Arrange
//        doThrow(new RuntimeException("Kafka connection failed"))
//            .when(eventProducer).send(any(EventDTO.class));
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> eventController.publishEvent(validEvent));
//    }
//
//    @Test
//    void testPublishEventWithComplexPayload() {
//        // Arrange
//        Map<String, Object> complexPayload = new HashMap<>();
//        complexPayload.put("orderId", "ORD-002");
//        complexPayload.put("items", java.util.List.of("item1", "item2"));
//        complexPayload.put("nested", Map.of("key", "value"));
//        validEvent.setPayload(complexPayload);
//
//        doNothing().when(eventProducer).send(any(EventDTO.class));
//
//        // Act
//        ResponseEntity<ApiResponse<EventDTO>> response = eventController.publishEvent(validEvent);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("SUCCESS", response.getBody().getStatus());
//    }
//}
//
