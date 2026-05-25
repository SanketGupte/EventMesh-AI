//package com.eventmesh.ingestionservice.producer;
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
//import org.springframework.kafka.support.SendResult;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//import com.eventmesh.ingestion.producer.EventProducer;
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
//            .timestamp(Instant.now())
//            .payload(new HashMap<>(Map.of("orderId", "ORD-TEST")))
//            .metadata(new HashMap<>(Map.of("correlationId", "corr-test")))
//            .build();
//    }
//
//    @Test
//    void testSendEventSuccess() {
//        SendResult<String, EventDTO> sendResult = mock(SendResult.class);
//
//        CompletableFuture<SendResult<String, EventDTO>> future =
//                CompletableFuture.completedFuture(sendResult);
//        // Arrange
//        when(kafkaTemplate.send(eq(KafkaTopics.RAW_EVENTS), anyString(), any(EventDTO.class)))
//            .thenReturn(future);
//
//        // Act
//        eventProducer.send(testEvent);
//
//        // Assert
//        verify(kafkaTemplate, times(1)).send(
//            eq(KafkaTopics.RAW_EVENTS),
//            eq(testEvent.getEventId()),
//            eq(testEvent)
//        );
//    }
//
//    @Test
//    void testSendEventWithNullEvent() {
//        // Act & Assert
//        try {
//            eventProducer.send(null);
//        } catch (NullPointerException e) {
//            verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
//        }
//    }
//
//    @Test
//    void testSendEventUsesCorrectTopic() {
//        SendResult<String, EventDTO> sendResult = mock(SendResult.class);
//
//        CompletableFuture<SendResult<String, EventDTO>> future =
//                CompletableFuture.completedFuture(sendResult);
//        // Arrange
//        when(kafkaTemplate.send(anyString(), anyString(), any(EventDTO.class)))
//            .thenReturn(future);
//
//        // Act
//        eventProducer.send(testEvent);
//
//        // Assert
//        verify(kafkaTemplate, times(1)).send(
//            eq(KafkaTopics.RAW_EVENTS),
//            anyString(),
//            any()
//        );
//    }
//
//    @Test
//    void testSendEventUsesEventIdAsKey() {
//        SendResult<String, EventDTO> sendResult = mock(SendResult.class);
//
//        CompletableFuture<SendResult<String, EventDTO>> future =
//                CompletableFuture.completedFuture(sendResult);
//        // Arrange
//        when(kafkaTemplate.send(anyString(), anyString(), any(EventDTO.class)))
//            .thenReturn(future);
//
//        // Act
//        eventProducer.send(testEvent);
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
