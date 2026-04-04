package com.motocart.order_microservice.order.kafka.listener;

import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.kafka.KafkaTopics;
import com.motocart.order_microservice.order.service.OrderManagementService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderManagementService orderManagementService;

    public OrderEventListener(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @KafkaListener(topics = KafkaTopics.ORDER_EVENTS, groupId = "order-group")
    public void listener(OrderEvent orderEvent) {
        orderManagementService.processOrderEvent(orderEvent);
    }
}
