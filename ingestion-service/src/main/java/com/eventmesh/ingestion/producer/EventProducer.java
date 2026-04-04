package com.eventmesh.ingestion.producer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventProducer {
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public void send(EventDTO event) {
        System.out.println("Sending Event to PROCESSED_EVENTS topic");
        kafkaTemplate.send(KafkaTopics.PROCESSED_EVENTS, event);
    }
}
