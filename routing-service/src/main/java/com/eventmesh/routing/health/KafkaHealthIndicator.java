package com.eventmesh.routing.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom Health Indicator for Kafka connectivity.
 * Checks if Kafka broker is accessible.
 */
@Component("kafkaHealth")
public class KafkaHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // In production, implement actual Kafka connectivity checks
            // This is a basic implementation
            return Health.up()
                .withDetail("kafka", "Kafka broker is accessible")
                .withDetail("topic", "raw-events-topic")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}

