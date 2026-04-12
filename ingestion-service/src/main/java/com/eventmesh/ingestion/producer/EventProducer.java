package com.eventmesh.ingestion.producer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventProducer {
    private  static  final Logger log = LoggerFactory.getLogger(EventProducer.class);
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public void send(EventDTO event) {
        log.info("Sending Event to PROCESSED_EVENTS topic");
        kafkaTemplate.send(KafkaTopics.PROCESSED_EVENTS, event);
    }
}
