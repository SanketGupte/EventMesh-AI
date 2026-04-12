package com.eventmesh.routing.consumer;

import com.eventmesh.common.dto.EventDTO;
import com.eventmesh.routing.enums.EventStatus;
import com.eventmesh.routing.service.BackoffStrategy;
import com.eventmesh.routing.service.EventLogService;
import com.eventmesh.routing.service.RetryPolicyService;
import com.eventmesh.routing.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeadLetterConsumer {
    private  static final Logger log = LoggerFactory.getLogger(DeadLetterConsumer.class);

    private final RoutingService routingService;
    private final EventLogService eventLogService;
    private final RetryPolicyService retryPolicyService;
    private final BackoffStrategy backoffStrategy;

    @KafkaListener(
            topics = "event.dlq",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventDTO event){
        String eventId = event.getEventId();

        if(!retryPolicyService.canRetry(eventId)){
            log.error("Max retries reached for event: {}", eventId);
            eventLogService.updateStatus(eventId, "DLQ", EventStatus.FAILED_PERMANENT);
            return;
        }

        int retryCount = retryPolicyService.getRetryCount(eventId);
        long delay = backoffStrategy.getBackoffDelay(retryCount);

        try{
            log.warn("Backing off {} ms before retry for event: {}", delay, eventId);
            Thread.sleep(delay); //simple delay ToDo: Improve later
            retryPolicyService.incrementRetryCount(eventId);
            log.warn("Retrying event {} attempt {}", eventId, retryPolicyService.getRetryCount(eventId));
            routingService.route(event);
            retryPolicyService.resetRetryCount(eventId);
            eventLogService.updateStatus(eventId, "RETRY_SUCCESS", EventStatus.RETRIED);
        }catch (Exception ex){
            log.error("Retry failed again for the event: {}", event.getEventId(), ex);
            eventLogService.updateStatus(eventId, "DLQ", EventStatus.FAILED_PERMANENT);
        }
    }
}
