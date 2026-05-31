package com.eventmesh.common.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for EventDTO.
 * Tests event data transfer object and validation.
 */
@ExtendWith(MockitoExtension.class)
class EventDTOTest {
    private static final Instant FIXED_TIMESTAMP = Instant.parse("2024-01-01T00:00:00Z");

    @Test
    void testEventDTOCreation() {
        // Arrange
        EventDTO event = EventDTO.builder()
            .eventId("evt-001")
            .eventType("ORDER_CREATED")
            .source("order-service")
            .timestamp(FIXED_TIMESTAMP)
            .payload(new HashMap<>(Map.of("orderId", "ORD-001")))
            .metadata(new HashMap<>(Map.of("correlationId", "corr-123")))
            .build();

        // Assert
        assertNotNull(event);
        assertEquals("evt-001", event.getEventId());
        assertEquals("ORDER_CREATED", event.getEventType());
        assertEquals("order-service", event.getSource());
    }

    @Test
    void testEventDTOWithNullPayload() {
        // Arrange
        EventDTO event = EventDTO.builder()
            .eventId("evt-002")
            .eventType("ORDER_CREATED")
            .source("order-service")
            .timestamp(FIXED_TIMESTAMP)
            .payload(null)
            .build();

        // Assert
        assertNotNull(event);
        assertNull(event.getPayload());
    }

    @Test
    void testEventDTOEquals() {
        // Arrange
        Map<String, Object> payload = new HashMap<>(Map.of("orderId", "ORD-001"));

        EventDTO event1 = EventDTO.builder()
            .eventId("evt-001")
            .eventType("ORDER_CREATED")
            .source("order-service")
            .timestamp(FIXED_TIMESTAMP)
            .payload(payload)
            .build();

        EventDTO event2 = EventDTO.builder()
            .eventId("evt-001")
            .eventType("ORDER_CREATED")
            .source("order-service")
            .timestamp(FIXED_TIMESTAMP)
            .payload(payload)
            .build();

        // Assert
        assertEquals(event1, event2);
    }

    @Test
    void testEventDTOWithComplexPayload() {
        // Arrange
        Map<String, Object> complexPayload = new HashMap<>();
        complexPayload.put("orderId", "ORD-001");
        complexPayload.put("items", java.util.List.of("item1", "item2"));
        complexPayload.put("nested", Map.of("key", "value"));

        EventDTO event = EventDTO.builder()
            .eventId("evt-003")
            .eventType("ORDER_CREATED")
            .source("order-service")
            .timestamp(FIXED_TIMESTAMP)
            .payload(complexPayload)
            .build();

        // Assert
        assertNotNull(event.getPayload());
        assertTrue(event.getPayload().containsKey("items"));
        assertTrue(event.getPayload().containsKey("nested"));
    }
}

