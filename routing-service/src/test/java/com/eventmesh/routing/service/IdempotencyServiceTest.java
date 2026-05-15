//package com.eventmesh.routing.service;
//
//import com.eventmesh.routing.repository.EventLogRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
///**
// * Unit Tests for IdempotencyService.
// * Tests duplicate event detection and prevention.
// */
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//class IdempotencyServiceTest {
//
//    @Mock
//    private EventLogRepository eventLogRepository;
//
//    @InjectMocks
//    private IdempotencyService idempotencyService;
//
//    private String testEventId;
//
//    @BeforeEach
//    void setUp() {
//        testEventId = "evt-id-123";
//    }
//
//    @Test
//    void testIsDuplicateEventReturnsFalseForNewEvent() {
//        // Arrange
//        when(eventLogRepository.existsByEventId(testEventId))
//            .thenReturn(false);
//
//        // Act
//        boolean isDuplicate = idempotencyService.isDuplicateEvent(testEventId);
//
//        // Assert
//        assertFalse(isDuplicate);
//    }
//
//    @Test
//    void testIsDuplicateEventReturnsTrueForExistingEvent() {
//        // Arrange
//        when(eventLogRepository.existsByEventId(testEventId))
//            .thenReturn(true);
//
//        // Act
//        boolean isDuplicate = idempotencyService.isDuplicateEvent(testEventId);
//
//        // Assert
//        assertTrue(isDuplicate);
//    }
//
//    @Test
//    void testRecordEventIdForIdempotency() {
//        // Arrange & Act
//        idempotencyService.recordEventId(testEventId);
//
//        // Assert
//        verify(eventLogRepository, times(1)).existsByEventId(testEventId);
//    }
//
//    @Test
//    void testIsDuplicateEventWithNullEventId() {
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> {
//            idempotencyService.isDuplicateEvent(null);
//        });
//    }
//
//    @Test
//    void testIsDuplicateEventWithEmptyEventId() {
//        // Arrange
//        when(eventLogRepository.existsByEventId(""))
//            .thenReturn(false);
//
//        // Act
//        boolean isDuplicate = idempotencyService.isDuplicateEvent("");
//
//        // Assert
//        assertFalse(isDuplicate);
//    }
//}
//
