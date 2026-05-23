package com.motocart.order_microservice.order.api.impl;

import com.motocart.library.common.annotation.MotocartAPI;
import com.motocart.library.common.dto.response.OrderResponseDTO;
import com.motocart.order_microservice.order.api.OrderResource;
import com.motocart.order_microservice.order.service.OrderManagementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@MotocartAPI("/order")
public class OrderResourceImpl implements OrderResource {

    private final OrderManagementService  orderManagementService;

    public OrderResourceImpl(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @Override
    @GetMapping("/_query/{orderId}")
    public OrderResponseDTO getOrderDetails(@PathVariable("orderId") int orderId) {
        return orderManagementService.getOrderDetails(orderId);
    }
}
