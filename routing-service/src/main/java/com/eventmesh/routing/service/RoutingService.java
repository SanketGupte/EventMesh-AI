package com.eventmesh.routing.service;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.ai.service.AiRoutingService;
import com.eventmesh.routing.entity.EventLog;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.exception.DuplicateEventException;
import com.eventmesh.routing.producer.DeadLetterProducer;
import com.eventmesh.routing.repository.EventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;

    public void route(EventDTO event){

        String eventId = event.getEventId();
        String eventType = event.getEventType();

        try{
            idempotencyService.checkAndCreate(event);
            String destinationTopic = null;
            boolean aiDecision = false;
            double confidence = 0.0;

            //1. Rule based Routing
            try{
                destinationTopic = routingRuleService.getDestinationTopic(event.getEventType());
            }catch(IllegalArgumentException ex){
                log.warn("No rule found for event type ={}, trying intelligent fallback", event.getEventType());
            }

            //2. Intelligent FallBackusing event_logs
            if(destinationTopic == null){
                List<EventLog> logs = eventLogRepository.findByEventType(eventType);
                if (!logs.isEmpty()) {

                    Map<String, Long> frequencyMap = logs.stream()
                            .collect(Collectors.groupingBy(
                                    EventLog::getDestinationTopic,
                                    Collectors.counting()
                            ));

                    Map.Entry<String, Long> bestMatch = frequencyMap.entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);

                    if (bestMatch != null) {
                        destinationTopic = bestMatch.getKey();
                        aiDecision = true;

                        long total = logs.size();
                        long matched = bestMatch.getValue();

                        confidence = (matched * 1.0) / total;

                        log.info("🤖 Learned routing: {} → {} (confidence: {})",
                                eventType, destinationTopic, confidence);
                    }
                }
            }

            //3. No Match -> DLQ
            if (destinationTopic == null) {
                log.error("No routing rule or historical match. Sending to DLQ. eventId={}", eventId);
                deadLetterProducer.sendToDLQ(event, new RuntimeException("No routing rule or fallback found"));
                eventLogService.updateStatus(eventId, "DLQ", EventStatus.FAILED);
                return;
            }


            log.info("Routing event to: {}", destinationTopic);
            kafkaTemplate.send(destinationTopic, event);
            eventLogService.updateStatus(
                    event.getEventId(),
                    destinationTopic,
                    EventStatus.ROUTED
            );
            persistLearning(event, destinationTopic, aiDecision, confidence);
            log.info("Event routed successfully: {}", eventId);
        } catch (IllegalArgumentException e){
            log.error("Routing fallback triggered | eventType={} | eventId={}", event.getEventType(), eventId);
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

    private void persistLearning(EventDTO event,
                                 String destination,
                                 boolean aiDecision,
                                 double confidence) {

        try {

            String payloadJson = objectMapper.writeValueAsString(event.getPayload());

            eventLogService.enrichEventLog(
                    event.getEventId(),
                    payloadJson,
                    aiDecision,
                    confidence
            );

        } catch (Exception e) {
            log.error("⚠️ Failed to persist learning for eventId={}", event.getEventId(), e);
        }
    }
}
