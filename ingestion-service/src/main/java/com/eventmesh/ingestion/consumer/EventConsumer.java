package com.eventmesh.ingestion.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.ingestion.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventConsumer {

    private  static  final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    private final EventProducer eventProducer;

    @KafkaListener(
            topics = KafkaTopics.RAW_EVENTS,
            groupId = "eventmesh-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventDTO event){
        log.info("Received Event: " + event.getEventType());

        eventProducer.send(event);

    }
}
