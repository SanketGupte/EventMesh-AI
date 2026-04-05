package com.eventmesh.routing.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="event_logs")
@Data
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String eventType;
    private String source;
    private String destinationTopic;
    private String status;
    private LocalDateTime timestamp;
}
