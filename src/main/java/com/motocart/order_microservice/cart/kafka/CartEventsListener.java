package com.motocart.order_microservice.cart.kafka;

import com.motocart.library.common.event.CartEvent;
import com.motocart.library.kafka.KafkaTopics;
import com.motocart.order_microservice.cart.service.CartManagementService;
import org.springframework.kafka.annotation.KafkaListener;

public class CartEventsListener {

    private final CartManagementService cartManagementService;

    public CartEventsListener(CartManagementService cartManagementService) {
        this.cartManagementService = cartManagementService;
    }

    @KafkaListener(topics = KafkaTopics.CART_EVENTS, groupId = "cart-group")
    public void listener(CartEvent cartEvent) {
        cartManagementService.addItemToCart(cartEvent);
    }
}
