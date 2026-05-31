package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.exception.DuplicateEventException;
import com.eventmesh.routing.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {
    private static  final Logger log = LoggerFactory.getLogger(IdempotencyService.class);
//    private final ConcurrentHashMap<String, Boolean> processedEvents = new ConcurrentHashMap<>();

    private final EventLogRepository eventLogRepository;
    private final EventLogService eventLogService;

    public void checkAndCreate(EventDTO event){
        try{
            EventLog logEntry = new EventLog();
            logEntry.setEventId(event.getEventId());
            logEntry.setEventType(event.getEventType());
            logEntry.setSource(event.getSource());
            logEntry.setStatus(EventStatus.RECEIVED);
            logEntry.setTimestamp(LocalDateTime.now());

            eventLogRepository.save(logEntry);

            log.info("New event accepted: {}", event.getEventId());
        } catch(DataIntegrityViolationException ex){
            log.warn("Duplicate event detected: {}", event.getEventId());
            throw new DuplicateEventException(event.getEventId());
        }
    }

}
