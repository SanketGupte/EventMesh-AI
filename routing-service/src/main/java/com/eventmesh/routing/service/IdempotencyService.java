package com.eventmesh.routing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {
    private static  final Logger log = LoggerFactory.getLogger(IdempotencyService.class);
    private final ConcurrentHashMap<String, Boolean> processedEvents = new ConcurrentHashMap<>();

    public boolean isDuplicate(String eventId){
        boolean result = processedEvents.containsKey(eventId);
        log.info("Duplicate eventId: {} result: {}", eventId, result);
        return result;
    }

    public void markProcessed(String eventId){
        log.info("Marking processed event to: {}", eventId);
        processedEvents.put(eventId, true);
    }
}
