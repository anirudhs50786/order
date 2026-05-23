package com.motocart.order_microservice.order.api;

import com.motocart.library.common.dto.response.OrderResponseDTO;

public interface OrderResource {

    OrderResponseDTO getOrderDetails(int orderId);
}
