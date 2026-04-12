package com.eventmesh.routing.controller;

import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-logs")
@RequiredArgsConstructor
public class EventLogController {
    private final EventLogService eventLogService;

    @GetMapping
    public List<EventLog> getAllLogs(){
        return eventLogService.getAllLogs();
    }

    @GetMapping("/status/{status}")
    public List<EventLog> getByStatus(@PathVariable("status") String status){
        return eventLogService.getStatus(status);
    }

    @GetMapping("/event/{eventType}")
    public List<EventLog> getByEventType(@PathVariable("eventType") String eventType){
        return eventLogService.getByEventType(eventType);
    }
}
