package com.eventmesh.routing.producer;

import com.eventmesh.common.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeadLetterProducer {
    private  static  final Logger log = LoggerFactory.getLogger(DeadLetterProducer.class);
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;
    private static  final String DLQ_TOPIC = "event.dlq";

    public void sendToDLQ(EventDTO event, Exception  ex) {
        log.error("Routing Failed for Event:  {}, Reason: {}", event.getEventType(), ex.getMessage());
        kafkaTemplate.send(DLQ_TOPIC, event);
    }
}
