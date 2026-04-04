package com.motocart.order_microservice.order.service;

import com.motocart.library.common.dto.request.BillerItemDTO;
import com.motocart.library.common.dto.request.BillerRequestDTO;
import com.motocart.library.common.dto.response.BillerResponseDTO;
import com.motocart.library.common.event.InventoryEvent;
import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.common.types.InventoryActionType;
import com.motocart.library.common.types.OrderEventType;
import com.motocart.order_microservice.cart.entity.CartEntity;
import com.motocart.order_microservice.cart.service.CartManagementService;
import com.motocart.order_microservice.integration.BillerServiceClient;
import com.motocart.order_microservice.order.entity.OrderEntity;
import com.motocart.order_microservice.order.kafka.producer.InventoryEventProducer;
import com.motocart.order_microservice.order.repository.OrderRepository;
import com.motocart.order_microservice.util.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderManagementService {

    private final CartManagementService cartManagementService;
    private final OrderRepository orderRepository;
    private final InventoryEventProducer inventoryEventProducer;
    private final BillerServiceClient billerServiceClient;

    public OrderManagementService(CartManagementService cartManagementService,
                                  OrderRepository orderRepository,
                                  InventoryEventProducer inventoryEventProducer, BillerServiceClient billerServiceClient) {
        this.cartManagementService = cartManagementService;
        this.orderRepository = orderRepository;
        this.inventoryEventProducer = inventoryEventProducer;
        this.billerServiceClient = billerServiceClient;
    }

    public void processOrderEvent(OrderEvent orderEvent) {
        switch (orderEvent.getOrderEventType()){
            case OrderEventType.ORDER_INITIATED -> processOrderInitiatedEvent(orderEvent);

            case OrderEventType.ORDER_CONFIRMED -> processOrderConfirmedEvent(orderEvent);

            case OrderEventType.PAYMENT_COMPLETED -> processPaymentCompletedEvent(orderEvent);

            case OrderEventType.PAYMENT_FAILED -> processPaymentFailedEvent(orderEvent);

            case OrderEventType.SHIPMENT_DISPATCHED -> processShipmentDispatchedEvent(orderEvent);

            case OrderEventType.ORDER_CANCELLED -> processOrderCancelledEvent(orderEvent);

            case OrderEventType.ORDER_DELIVERED -> processOrderDeliveredEvent(orderEvent);
        }
    }

    private void processOrderInitiatedEvent(OrderEvent orderEvent) {

        CartEntity cartEntity = cartManagementService.getCartByUserId(orderEvent.getUserId());
        BillerRequestDTO billerRequestDTO = Mapper.createBillerRequestDTO(cartEntity, orderEvent.getUserId());
        BillerResponseDTO billerResponseDTO = billerServiceClient.generateBill(billerRequestDTO);
        OrderEntity orderEntity = orderRepository.save(Mapper.copyToOrderEntity(cartEntity, orderEvent, billerResponseDTO));
        inventoryEventProducer.sendInventoryEvent(Mapper.toReserveInventoryEvent(orderEntity));
    }

    private void processOrderConfirmedEvent(OrderEvent orderEvent) {
        // process order confirmed event
    }

    private void processPaymentCompletedEvent(OrderEvent orderEvent) {
        // process payment completed event
    }

    private void processOrderCancelledEvent(OrderEvent orderEvent) {
        // process order cancelled event
    }

    private void processPaymentFailedEvent(OrderEvent orderEvent) {
        //
    }

    private void processOrderDeliveredEvent(OrderEvent orderEvent) {
        // process order delivered event
    }

    private void processShipmentDispatchedEvent(OrderEvent orderEvent) {
        // process shipment dispatched event
    }
}
