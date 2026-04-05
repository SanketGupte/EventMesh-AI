package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventLogService {
    private final EventLogRepository repository;
    public  void logEvent(EventDTO event, String destinationTopic, String status){
        EventLog logEntry = new EventLog();
        logEntry.setEventId(event.getEventId());
        logEntry.setEventType(event.getEventType());
        logEntry.setSource(event.getSource());
        logEntry.setDestinationTopic(destinationTopic);
        logEntry.setStatus(status);
        repository.save(logEntry);
    }

    public List<EventLog> getAllLogs(){
        return repository.findAll();
    }

    public List<EventLog> getStatus(String status){
        return repository.findAll()
                .stream().filter(log -> log.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    public List<EventLog> getByEventType(String eventType){
        return repository.findAll()
                .stream()
                .filter(log -> log.getEventType().equalsIgnoreCase(eventType))
                .toList();
    }
}
