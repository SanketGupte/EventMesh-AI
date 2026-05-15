package com.eventmesh.routing.health;

import com.eventmesh.routing.repository.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom Health Indicator for Database connectivity.
 * Checks if PostgreSQL database is accessible.
 */
@Component("databaseHealth")
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private EventLogRepository eventLogRepository;

    @Override
    public Health health() {
        try {
            // Try to count records to verify database connection
            long eventCount = eventLogRepository.count();
            return Health.up()
                .withDetail("database", "PostgreSQL database is accessible")
                .withDetail("eventLogCount", eventCount)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", "Database connection failed: " + e.getMessage())
                .build();
        }
    }
}

