package com.eventmesh.ingestion.controller;

import com.eventmesh.common.ApiResponse;
import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Qualifier("eventKafkaTemplate")
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;


    @PostMapping
    public ResponseEntity<ApiResponse<EventDTO>> publishEvent(@RequestBody @Valid EventDTO event){
        logger.info("📥 Received event: {} of type {}", event.getEventId(), event.getEventType());
        try{
            //Wait for Kafka ACK (important for reliability)
            kafkaTemplate.send(KafkaTopics.RAW_EVENTS, event).get();

            logger.info("Event successfully published to Kafka. EventId: {}", event.getEventId());
//            return ResponseEntity.ok("Event accepted successfully. EventId :" + event.getEventId());
            return ResponseEntity.ok(ApiResponse.success("Event published Successfully", event));
        } catch (Exception ex){
            logger.error("Error while publishing event to Kafka. EventId: {}", event.getEventId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Error while publishing event to Kafka. EventId: "));
        }
    }

}
