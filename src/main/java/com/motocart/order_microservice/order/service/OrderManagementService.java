package com.motocart.order_microservice.order.service;

import com.motocart.library.common.dto.request.BillerRequestDTO;
import com.motocart.library.common.dto.response.BillerResponseDTO;
import com.motocart.library.common.dto.response.OrderResponseDTO;
import com.motocart.library.common.event.InventoryEvent;
import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.common.exception.GlobalException;
import com.motocart.library.common.types.InventoryActionType;
import com.motocart.library.common.types.OrderEventType;
import com.motocart.library.security.authentication.EntitlementService;
import com.motocart.order_microservice.cart.entity.CartEntity;
import com.motocart.order_microservice.cart.service.CartManagementService;
import com.motocart.order_microservice.integration.BillerServiceClient;
import com.motocart.order_microservice.order.entity.OrderEntity;
import com.motocart.order_microservice.order.kafka.producer.InventoryEventProducer;
import com.motocart.order_microservice.order.repository.OrderRepository;
import com.motocart.order_microservice.util.Mapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderManagementService {

    private final CartManagementService cartManagementService;
    private final OrderRepository orderRepository;
    private final InventoryEventProducer inventoryEventProducer;
    private final BillerServiceClient billerServiceClient;
    private final OrderValidatorService orderValidatorService;

    public OrderManagementService(CartManagementService cartManagementService,
                                  OrderRepository orderRepository,
                                  InventoryEventProducer inventoryEventProducer,
                                  BillerServiceClient billerServiceClient,
                                  OrderValidatorService orderValidatorService) {
        this.cartManagementService = cartManagementService;
        this.orderRepository = orderRepository;
        this.inventoryEventProducer = inventoryEventProducer;
        this.billerServiceClient = billerServiceClient;
        this.orderValidatorService = orderValidatorService;
    }

    public OrderResponseDTO getOrderDetails(int order) {
        OrderEntity orderEntity  = orderRepository.findById(order).orElseThrow(() -> new GlobalException("Order not found"));
        EntitlementService.isResourceOwner(orderEntity.getUserId());
        return Mapper.toOrderResponseDTO(orderEntity);
    }

    public void processOrderEvent(OrderEvent orderEvent) {
        orderValidatorService.validateOrderEvent(orderEvent);
        switch (orderEvent.getOrderEventType()){
            case OrderEventType.ORDER_INITIATED -> processOrderInitiatedEvent(orderEvent);

            // COD
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

        orderRepository.findById(orderEvent.getOrderId()).ifPresent(orderEntity -> {
            orderEntity.setOrderStatus(orderEvent.getOrderStatus());
            orderEntity.setUpdatedAt(Instant.now());
            orderRepository.save(orderEntity);
            inventoryEventProducer.sendInventoryEvent(InventoryEvent.builder()
                    .actionType(InventoryActionType.DEDUCT)
                    .orderId(orderEvent.getOrderId())
                    .build());
            //send email
        });
    }

    private void processPaymentCompletedEvent(OrderEvent orderEvent) {

        OrderEntity orderEntity = getOrderEntity(orderEvent.getOrderId());
        updateOrderStatus(orderEntity, orderEvent);
        // send payment success email / notification if required
    }

    private void processOrderCancelledEvent(OrderEvent orderEvent) {

        OrderEntity orderEntity = getOrderEntity(orderEvent.getOrderId());
        updateOrderStatus(orderEntity, orderEvent);
        inventoryEventProducer.sendInventoryEvent(InventoryEvent.builder()
                .actionType(InventoryActionType.RELEASE)
                .orderId(orderEvent.getOrderId())
                .build());
        // send cancellation email / notification if required
    }

    private void processPaymentFailedEvent(OrderEvent orderEvent) {

        OrderEntity orderEntity = getOrderEntity(orderEvent.getOrderId());
        updateOrderStatus(orderEntity, orderEvent);
        inventoryEventProducer.sendInventoryEvent(InventoryEvent.builder()
                .actionType(InventoryActionType.RELEASE)
                .orderId(orderEvent.getOrderId())
                .build());
        // send payment failed email / notification if required
    }

    private void processOrderDeliveredEvent(OrderEvent orderEvent) {

        OrderEntity orderEntity = getOrderEntity(orderEvent.getOrderId());
        updateOrderStatus(orderEntity, orderEvent);
        // send delivery confirmation email / notification if required
    }

    private void processShipmentDispatchedEvent(OrderEvent orderEvent) {

        OrderEntity orderEntity = getOrderEntity(orderEvent.getOrderId());
        updateOrderStatus(orderEntity, orderEvent);
        // send shipment dispatched email / notification if required
    }

    private OrderEntity getOrderEntity(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException("Order not found"));
    }

    private void updateOrderStatus(OrderEntity orderEntity, OrderEvent orderEvent) {
        orderEntity.setOrderStatus(orderEvent.getOrderStatus());
        orderEntity.setUpdatedAt(Instant.now());
        orderRepository.save(orderEntity);
    }
}
