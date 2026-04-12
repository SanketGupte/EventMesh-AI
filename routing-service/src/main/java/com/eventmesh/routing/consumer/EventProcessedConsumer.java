package com.eventmesh.routing.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventProcessedConsumer {
    private  static  final Logger log = LoggerFactory.getLogger(EventProcessedConsumer.class);
    private  final RoutingService routingService;

    @KafkaListener(
            topics = KafkaTopics.PROCESSED_EVENTS,
            groupId = "eventmesh-routing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventDTO event){
        log.info("Routing Service Received Event: " + event.getEventType());
        routingService.route(event);
    }
}
