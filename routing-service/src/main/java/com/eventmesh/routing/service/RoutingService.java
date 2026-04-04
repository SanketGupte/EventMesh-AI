package com.eventmesh.routing.service;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {
    private  final KafkaTemplate<String, EventDTO> kafkaTemplate;
//    private  final RoutingService routingService;

    public void route(EventDTO event){
        String destinationTopic = determineTopic(event);
        System.out.println("Rounting event to: " + destinationTopic);
        kafkaTemplate.send(destinationTopic, event);
    }

    private String determineTopic(EventDTO event){
        String eventType = event.getEventType();

        if("ORDER_CREATED".equals(eventType)){
            return KafkaTopics.ROUTE_PAYMENT;
        }
        return KafkaTopics.ROUTE_DEFAULT;
    }
}
