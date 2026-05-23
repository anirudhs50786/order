package com.motocart.order_microservice.order.kafka.producer;

import com.motocart.library.common.event.NotificationEvent;
import com.motocart.library.kafka.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public NotificationEventProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotificationEvent(NotificationEvent notificationEvent){
        kafkaTemplate.send(KafkaTopics.NOTIFICATION_EVENTS, notificationEvent);
    }
}
