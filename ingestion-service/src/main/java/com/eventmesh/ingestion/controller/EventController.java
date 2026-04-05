package com.eventmesh.ingestion.controller;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    @PostMapping
    public String publishEvent(@RequestBody EventDTO event){
        System.out.println("Received API Event: " + event.getEventType());
        kafkaTemplate.send(KafkaTopics.RAW_EVENTS, event);
        return "Event published to Kafka";
    }

}
