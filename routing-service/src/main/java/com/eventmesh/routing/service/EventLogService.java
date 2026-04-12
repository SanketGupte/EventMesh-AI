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
    public  void logEvent(EventDTO event, String destinationTopic, EventStatus status){
        try{
            EventLog logEntry = new EventLog();
            logEntry.setEventId(event.getEventId());
            logEntry.setEventType(event.getEventType());
            logEntry.setSource(event.getSource());
            logEntry.setDestinationTopic(destinationTopic);
            logEntry.setStatus(status);
            repository.save(logEntry);
        } catch (Exception ex){
            log.error("Failed to log event {} due to {}", event, ex.getMessage());
        }
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
}
