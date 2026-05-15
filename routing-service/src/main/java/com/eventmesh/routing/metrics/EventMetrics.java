package com.eventmesh.routing.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

/**
 * Metrics collector for event processing.
 * Tracks various system metrics for monitoring and observability.
 */
@Component
public class EventMetrics {

    private final Counter eventsReceived;
    private final Counter eventsProcessed;
    private final Counter evennsFailedCount;
    private final Counter eventsRetried;
    private final Timer processingDuration;

    public EventMetrics(MeterRegistry meterRegistry) {
        // Event counters
        this.eventsReceived = Counter.builder("eventmesh.events.received")
            .description("Total number of events received")
            .register(meterRegistry);

        this.eventsProcessed = Counter.builder("eventmesh.events.processed.success")
            .description("Total number of events processed successfully")
            .register(meterRegistry);

        this.evennsFailedCount = Counter.builder("eventmesh.events.processed.failed")
            .description("Total number of events that failed processing")
            .register(meterRegistry);

        this.eventsRetried = Counter.builder("eventmesh.events.retried")
            .description("Total number of event retries")
            .register(meterRegistry);

        // Processing time tracker
        this.processingDuration = Timer.builder("eventmesh.event.processing.duration")
            .description("Time taken to process an event")
            .register(meterRegistry);
    }

    public void recordEventReceived() {
        eventsReceived.increment();
    }

    public void recordEventProcessed() {
        eventsProcessed.increment();
    }

    public void recordEventFailed() {
        evennsFailedCount.increment();
    }

    public void recordEventRetried() {
        eventsRetried.increment();
    }

    public Timer.Sample getProcessingTimer() {
        return Timer.start();
    }

    public void recordProcessingTime(Timer.Sample sample) {
        sample.stop(processingDuration);
    }
}

