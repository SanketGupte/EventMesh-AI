package com.eventmesh.routing.entity;

import com.eventmesh.routing.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="event_logs", uniqueConstraints = {@UniqueConstraint(columnNames = "event_id")})
@Data
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="event_id", nullable = false, unique = true)
    private String eventId;
    private String eventType;
    private String source;
    private String destinationTopic;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    private LocalDateTime timestamp;
}
