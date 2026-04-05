package com.eventmesh.routing.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventProcessedConsumer {
    private  final RoutingService routingService;

    @KafkaListener(
            topics = KafkaTopics.PROCESSED_EVENTS,
            groupId = "eventmesh-routing-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventDTO event){
        System.out.println("Routing Service Received Event: " + event.getEventType());
        routingService.route(event);
    }
}
