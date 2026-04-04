package com.eventmesh.ingestion.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.ingestion.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventConsumer {

    private final EventProducer eventProducer;

    @KafkaListener(
            topics = KafkaTopics.RAW_EVENTS,
            groupId = "eventmesh-group"
    )
    public void consume(EventDTO event){
        System.out.println("Received Event: " + event.getEventType());

        eventProducer.send(event);

    }
}
