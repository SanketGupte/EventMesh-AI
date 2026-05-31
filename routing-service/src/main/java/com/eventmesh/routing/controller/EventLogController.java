package com.eventmesh.routing.controller;

import com.eventmesh.common.ApiResponse;
import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<EventLog>>> getAllLogs(){
        List<EventLog> logs = eventLogService.getAllLogs();
        return ResponseEntity.ok(
                ApiResponse.success("Event logs fetched successfully", logs)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EventLog>>> getByStatus(@PathVariable("status") String status){
        List<EventLog> logs = eventLogService.getStatus(status);
        return ResponseEntity.ok(
                ApiResponse.success("Event logs fetched successfully", logs)
        );
    }

    @GetMapping("/event/{eventType}")
    public ResponseEntity<ApiResponse<List<EventLog>>> getByEventType(@PathVariable("eventType") String eventType){
        List<EventLog> logs = eventLogService.getByEventType(eventType);
        return ResponseEntity.ok(
                ApiResponse.success("Event logs fetched successfully", logs)
        );
    }
}
