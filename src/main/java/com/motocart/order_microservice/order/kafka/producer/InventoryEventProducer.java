package com.motocart.order_microservice.order.kafka.producer;

import com.motocart.library.common.event.InventoryEvent;
import com.motocart.library.kafka.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventProducer {

    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryEvent(InventoryEvent inventoryEvent) {
        kafkaTemplate.send(KafkaTopics.INVENTORY_EVENTS, inventoryEvent);
    }
}
