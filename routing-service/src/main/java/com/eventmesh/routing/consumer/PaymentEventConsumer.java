package com.eventmesh.routing.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {
    private  static  final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    @KafkaListener(
            topics = KafkaTopics.ROUTE_PAYMENT,
            groupId = "eventmesh-group"
    )
    public void consume(EventDTO event){
        log.info("Payment Service received event");
        log.info("Event Type: " + event.getEventType());
        log.info("Processing Payment for payload: " + event.getPayload());

        // Simulate processing
        processPayment(event);
    }

    private void processPayment(EventDTO event){
        log.info("Payment processed successfully for eventId: " + event.getEventId());
    }

}
