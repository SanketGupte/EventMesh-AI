//package com.eventmesh.ingestion.producer;
//
//import com.eventmesh.common.constants.KafkaTopics;
//import com.eventmesh.common.dto.EventDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit Tests for EventProducer.
// * Tests Kafka event publishing functionality.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class EventProducerTest {
//
//    @Mock
//    private KafkaTemplate<String, EventDTO> kafkaTemplate;
//
//    @InjectMocks
//    private EventProducer eventProducer;
//
//    private EventDTO testEvent;
//
//    @BeforeEach
//    void setUp() {
//        testEvent = EventDTO.builder()
//            .eventId("evt-test-001")
//            .eventType("ORDER_CREATED")
//            .source("test-service")
//            .timestamp(LocalDateTime.now())
//            .payload(new HashMap<>(Map.of("orderId", "ORD-TEST")))
//            .metadata(new HashMap<>(Map.of("correlationId", "corr-test")))
//            .build();
//    }
//
//    @Test
//    void testSendEventSuccess() {
//        // Arrange
//        when(kafkaTemplate.send(eq(KafkaTopics.RAW_EVENTS_TOPIC), anyString(), any(EventDTO.class)))
//            .thenReturn(mock(org.springframework.util.concurrent.ListenableFuture.class));
//
//        // Act
//        eventProducer.sendEvent(testEvent);
//
//        // Assert
//        verify(kafkaTemplate, times(1)).send(
//            eq(KafkaTopics.RAW_EVENTS_TOPIC),
//            eq(testEvent.getEventId()),
//            eq(testEvent)
//        );
//    }
//
//    @Test
//    void testSendEventWithNullEvent() {
//        // Act & Assert
//        try {
//            eventProducer.sendEvent(null);
//        } catch (NullPointerException e) {
//            verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
//        }
//    }
//
//    @Test
//    void testSendEventUsesCorrectTopic() {
//        // Arrange
//        when(kafkaTemplate.send(anyString(), anyString(), any(EventDTO.class)))
//            .thenReturn(mock(org.springframework.util.concurrent.ListenableFuture.class));
//
//        // Act
//        eventProducer.sendEvent(testEvent);
//
//        // Assert
//        verify(kafkaTemplate, times(1)).send(
//            eq(KafkaTopics.RAW_EVENTS_TOPIC),
//            anyString(),
//            any()
//        );
//    }
//
//    @Test
//    void testSendEventUsesEventIdAsKey() {
//        // Arrange
//        when(kafkaTemplate.send(anyString(), anyString(), any(EventDTO.class)))
//            .thenReturn(mock(org.springframework.util.concurrent.ListenableFuture.class));
//
//        // Act
//        eventProducer.sendEvent(testEvent);
//
//        // Assert
//        verify(kafkaTemplate, times(1)).send(
//            anyString(),
//            eq(testEvent.getEventId()),
//            any()
//        );
//    }
//}
//
