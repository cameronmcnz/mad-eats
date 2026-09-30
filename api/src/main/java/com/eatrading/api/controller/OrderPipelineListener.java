package com.eatrading.api.controller;
 
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.eatrading.api.dto.OrderTransactionRequest;

@Component 
public class OrderPipelineListener {

    private final KafkaTemplate<String, OrderTransactionRequest> kafkaTemplate;

    public OrderPipelineListener(
            KafkaTemplate<String, OrderTransactionRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "orders.incoming", groupId = "order-validator")
    public void validateOrder(OrderTransactionRequest request) throws Exception {
        System.out.println("Received incoming order for validation: " + request.TRACKING_ID);

        Thread.sleep(10_000);

        kafkaTemplate.send(
                "orders.unvalidated",
                String.valueOf(request.TRACKING_ID),
                request
        ).get();

        System.out.println("Sent order to orders.unvalidated: " + request.TRACKING_ID);
    }

    @KafkaListener(topics = "orders.unvalidated", groupId = "order-processor")
    public void processOrder(OrderTransactionRequest request) throws Exception {
        System.out.println("Received unvalidated order for processing: " + request.TRACKING_ID);

        Thread.sleep(10_000);

        kafkaTemplate.send(
                "orders.unprocessed",
                String.valueOf(request.TRACKING_ID),
                request
        ).get();

        System.out.println("Sent order to orders.unprocessed: " + request.TRACKING_ID);
    }
}