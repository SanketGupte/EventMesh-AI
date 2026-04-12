package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.exception.DuplicateEventException;
import com.eventmesh.routing.producer.DeadLetterProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//import java.time.LocalDateTime;

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

//        //Duplicate check
//        if(idempotencyService.isDuplicate(eventId)){
//            log.warn("Duplicate events detected: {}", eventId);
//            return;
//        }

        try{
            idempotencyService.checkAndCreate(event);
            String destinationTopic = routingRuleService.getDestinationTopic(event.getEventType());
            log.info("Rounting event to: {}", destinationTopic);
            kafkaTemplate.send(destinationTopic, event);
            eventLogService.updateStatus(
                    event.getEventId(),
                    destinationTopic,
                    EventStatus.ROUTED
            );
            log.info("Event routed successfully: {}", eventId);
        } catch (IllegalArgumentException e){
            log.error("No routing Rule found. Sending to DLQ. Event: {}", eventId);
            deadLetterProducer.sendToDLQ(event, e);
            eventLogService.updateStatus(eventId, "DLQ", EventStatus.FAILED);
        } catch(DuplicateEventException ex){
            log.warn("Duplicate events detected: {}", eventId);
        }
        catch(Exception e){
            log.error("Routing Failed for event: {}", eventId, e);
            deadLetterProducer.sendToDLQ(event, e);
            eventLogService.updateStatus(eventId, "DLQ", EventStatus.FAILED);
        }
    }
}
