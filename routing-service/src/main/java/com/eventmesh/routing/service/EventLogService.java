package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventLogService {
    private  static  final Logger log = LoggerFactory.getLogger(EventLogService.class);
    private final EventLogRepository repository;

    public void updateStatus(String eventId, String destinationTopic, EventStatus eventStatus){
        EventLog eventLog = repository.findByEventId(eventId).orElseThrow(() -> new RuntimeException("Event not found:" + eventId));
        eventLog.setDestinationTopic(destinationTopic);
        eventLog.setStatus(eventStatus);

        repository.save(eventLog);
        log.info("Event updated: {} -> {}", eventId, eventStatus);
    }

    public List<EventLog> getAllLogs(){
        return repository.findAll();
    }

    public List<EventLog> getStatus(String status){
        return repository.findAll()
                .stream().filter(log -> log.getStatus().toString().equalsIgnoreCase(status))
                .toList();
    }

    public List<EventLog> getByEventType(String eventType){
        return repository.findAll()
                .stream()
                .filter(log -> log.getEventType().equalsIgnoreCase(eventType))
                .toList();
    }

    public void enrichEventLog(String eventId,
                               String payload,
                               boolean aiDecision,
                               double confidence) {

        EventLog log = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        log.setPayload(payload);
        log.setAiDecision(aiDecision);
        log.setConfidenceScore(confidence);

        repository.save(log);
    }
}
