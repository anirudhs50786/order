package com.motocart.order_microservice.order.service;

import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.common.exception.GlobalException;
import com.motocart.library.common.types.OrderEventType;
import org.springframework.stereotype.Service;

@Service
public class OrderValidatorService {

    public void validateOrderEvent(OrderEvent orderEvent) {
        validateBasicOrderEventShape(orderEvent);
    }

    private static void validateBasicOrderEventShape(OrderEvent orderEvent) {
        if (orderEvent == null) {
            throw new GlobalException("Order event cannot be null");
        }

        if (orderEvent.getOrderEventType() == null) {
            throw new GlobalException("Order event type is required");
        }

        if (orderEvent.getUserId() <= 0) {
            throw new GlobalException("Valid user id is required");
        }
        if (orderEvent.getOrderId() <= 0 && orderEvent.getOrderEventType() != OrderEventType.ORDER_INITIATED) {
            throw new GlobalException("Valid order id is required");
        }
        if (orderEvent.getOrderStatus() == null) {
            throw new GlobalException("Order status is required");
        }
    }
}
