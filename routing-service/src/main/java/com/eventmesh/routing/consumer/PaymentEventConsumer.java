package com.eventmesh.routing.consumer;

import com.eventmesh.common.constants.KafkaTopics;
import com.eventmesh.common.dto.EventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    @KafkaListener(
            topics = KafkaTopics.ROUTE_PAYMENT,
            groupId = "eventmesh-group"
    )
    public void consume(EventDTO event){
        System.out.println("Payment Service received event");
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Processing Payment for payload: " + event.getPayload());

        // Simulate processing
        processPayment(event);
    }

    private void processPayment(EventDTO event){
        System.out.println("Payment processed successfully for eventId: " + event.getEventId());
    }

}
