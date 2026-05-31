package com.eventmesh.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Structured logging utility for consistent log formatting across services.
 * Integrates with MDC for correlation tracking.
 */
@Component
public class StructuredLogger {

    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);

    /**
     * Log event ingestion
     */
    public void logEventIngestion(String eventId, String eventType, String correlationId) {
        MDC.put("eventId", eventId);
        MDC.put("correlationId", correlationId);
        logger.info("Event ingested: eventType={}", eventType);
        MDC.clear();
    }

    /**
     * Log event routing
     */
    public void logEventRouting(String eventId, String eventType, String destination, String correlationId) {
        MDC.put("eventId", eventId);
        MDC.put("correlationId", correlationId);
        logger.info("Event routed: eventType={}, destination={}", eventType, destination);
        MDC.clear();
    }

    /**
     * Log event processing failure
     */
    public void logEventProcessingFailure(String eventId, String eventType, String reason, int retryCount, String correlationId) {
        MDC.put("eventId", eventId);
        MDC.put("correlationId", correlationId);
        logger.error("Event processing failed: eventType={}, reason={}, retryCount={}", eventType, reason, retryCount);
        MDC.clear();
    }

    /**
     * Log event retry
     */
    public void logEventRetry(String eventId, int retryCount, long backoffMs, String correlationId) {
        MDC.put("eventId", eventId);
        MDC.put("correlationId", correlationId);
        logger.warn("Event retry scheduled: retryCount={}, backoffMs={}", retryCount, backoffMs);
        MDC.clear();
    }

    /**
     * Log event to DLQ
     */
    public void logEventToDLQ(String eventId, String eventType, String reason, String correlationId) {
        MDC.put("eventId", eventId);
        MDC.put("correlationId", correlationId);
        logger.error("Event moved to DLQ: eventType={}, reason={}", eventType, reason);
        MDC.clear();
    }

    /**
     * Log service operation
     */
    public void logServiceOperation(String operation, String service, long durationMs) {
        logger.debug("Service operation completed: operation={}, service={}, durationMs={}", operation, service, durationMs);
    }
}

