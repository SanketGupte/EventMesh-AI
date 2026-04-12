package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.producer.DeadLetterProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoutingService {
    private  static  final Logger log = LoggerFactory.getLogger(RoutingService.class);
    private  final KafkaTemplate<String, EventDTO> kafkaTemplate;
    private final EventLogService eventLogService;
    private final DeadLetterProducer deadLetterProducer;
    private final IdempotencyService idempotencyService;
    private final RoutingRuleService routingRuleService;

    public void route(EventDTO event){

        String eventId = event.getEventId();

        //Duplicate check
        if(idempotencyService.isDuplicate(eventId)){
            log.warn("Duplicate events detected: {}", eventId);
            return;
        }

        try{
            String destinationTopic = routingRuleService.getDestinationTopic(event.getEventType());
            log.info("Rounting event to: {}", destinationTopic);
            kafkaTemplate.send(destinationTopic, event);
            idempotencyService.markProcessed(eventId);
            eventLogService.logEvent(event, destinationTopic, EventStatus.ROUTED);
        } catch (IllegalArgumentException e){
            log.error("No routing Rule found. Sending to DLQ. Event: {}", eventId);
            deadLetterProducer.sendToDLQ(event, e);
            eventLogService.logEvent(event, "DLQ", EventStatus.FAILED);
        }
        catch(Exception e){
            log.error("Routing Failed for event: {}", eventId, e);
            deadLetterProducer.sendToDLQ(event, e);
            eventLogService.logEvent(event, "DLQ", EventStatus.FAILED);
        }
    }

//    private String determineTopic(EventDTO event){
//        String eventType = event.getEventType();
//
//        if("ORDER_CREATED".equals(eventType)){
//            return KafkaTopics.ROUTE_PAYMENT;
//        }
//        return KafkaTopics.ROUTE_DEFAULT;
//    }
}
