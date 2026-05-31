package com.motocart.order_microservice.util;

import com.motocart.library.common.dto.CartDTO;
import com.motocart.library.common.dto.CartItemsDTO;
import com.motocart.library.common.dto.request.BillerItemDTO;
import com.motocart.library.common.dto.request.BillerRequestDTO;
import com.motocart.library.common.dto.response.BillerResponseDTO;
import com.motocart.library.common.dto.response.OrderItemResponseDTO;
import com.motocart.library.common.dto.response.OrderResponseDTO;
import com.motocart.library.common.event.CartEvent;
import com.motocart.library.common.event.InventoryEvent;
import com.motocart.library.common.event.NotificationEvent;
import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.common.types.InventoryActionType;
import com.motocart.library.common.types.NotificationType;
import com.motocart.order_microservice.cart.entity.CartEntity;
import com.motocart.order_microservice.cart.entity.CartItemEntity;
import com.motocart.order_microservice.document.vo.summary.OrderItemVO;
import com.motocart.order_microservice.document.vo.summary.OrderSummaryVO;
import com.motocart.order_microservice.order.entity.OrderEntity;
import com.motocart.order_microservice.order.entity.OrderItemsEntity;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Mapper {

    public static CartItemEntity toCartItemEntity(CartEvent cartEvent) {
        return CartItemEntity.builder()
                .productId(cartEvent.getProductId())
                .addedAt(Instant.now())
                .productName(cartEvent.getProductName())
                .productPrice(cartEvent.getProductPrice())
                .imageUrl(cartEvent.getImageUrl())
                .quantity(cartEvent.getQuantity())
                .build();
    }

    private static List<CartItemsDTO> toCartItemsDTO(List<CartItemEntity> cartItemEntityList) {
        List<CartItemsDTO> cartItemsDTOS = new ArrayList<>();
        for (var cartItemEntity : cartItemEntityList) {
            cartItemsDTOS.add(CartItemsDTO.builder()
                            .productId(cartItemEntity.getProductId())
                            .productName(cartItemEntity.getProductName())
                            .productPrice(cartItemEntity.getProductPrice())
                            .quantity(cartItemEntity.getQuantity())
                            .imageUrl(cartItemEntity.getImageUrl())
                            .addedAt(cartItemEntity.getAddedAt())
                            .cartItemId(cartItemEntity.getCartItemId())
                            .build());
        }
        return cartItemsDTOS;
    }

    public static CartDTO  toCartDTO(CartEntity cartEntity) {
        return CartDTO.builder()
                .cartId(cartEntity.getCartId())
                .createdAt(cartEntity.getCreatedAt())
                .updatedAt(cartEntity.getUpdatedAt())
                .cartItems(toCartItemsDTO(cartEntity.getCartItems()))
                .build();
    }

    public static OrderEntity copyToOrderEntity(CartEntity cartEntity, OrderEvent orderEvent, BillerResponseDTO billerResponseDTO) {
        OrderEntity orderEntity = OrderEntity.builder()
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .totalAmount(billerResponseDTO.getTotalAmount())
                .deliveryCharges(billerResponseDTO.getDeliveryCharges())
                .platformFees(billerResponseDTO.getPlatformFees())
                .discountAmount(billerResponseDTO.getCouponDiscount() + billerResponseDTO.getOfferDiscount())
                .subTotal(billerResponseDTO.getSubTotal())
                .userId(cartEntity.getUserId())
                .orderStatus(orderEvent.getOrderStatus())
                .orderItems(toOrderItemList(cartEntity.getCartItems()))
                .build();
        orderEntity.getOrderItems().forEach(orderItem -> orderItem.setOrder(orderEntity));
        return orderEntity;
    }

    private static List<OrderItemsEntity> toOrderItemList(List<CartItemEntity> cartItemEntities) {
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();
        for(var item : cartItemEntities) {
            orderItemsEntities.add(OrderItemsEntity.builder()
                    .lineTotal(item.getQuantity() * item.getProductPrice())
                    .productName(item.getProductName())
                    .productPrice(item.getProductPrice())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .imageUrl(item.getImageUrl())
                    .build());
        }
        return orderItemsEntities;
    }

    public static InventoryEvent toReserveInventoryEvent(OrderEntity orderEntity) {
        List<InventoryEvent.ProductQuantityPair> productQuantityPairs = orderEntity.getOrderItems().stream()
                .map(item -> new InventoryEvent.ProductQuantityPair(item.getProductId(), item.getQuantity()))
                .toList();
        return InventoryEvent.builder()
                .orderId(orderEntity.getOrderId())
                .actionType(InventoryActionType.RESERVE)
                .productQuantityPairs(productQuantityPairs)
                .build();
    }

    public static BillerRequestDTO createBillerRequestDTO(CartEntity cartEntity, int userId) {
        return BillerRequestDTO.builder()
                .userId(userId)
                .items(cartEntity.getCartItems().stream().map(item ->
                        BillerItemDTO.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build()
                ).toList())
                .build();
    }

    public static OrderSummaryVO toOrderSummaryVO(OrderEntity orderEntity) {
        List<OrderItemVO> items = orderEntity.getOrderItems().stream()
                .map(item -> OrderItemVO.builder()
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .productPrice(item.getProductPrice())
                        .lineTotal(item.getLineTotal())
                        .imageUrl(item.getImageUrl())
                        .build())
                .toList();

        return OrderSummaryVO.builder()
                .orderId(orderEntity.getOrderId())
                .orderStatus(orderEntity.getOrderStatus().toString())
                .createdAt(orderEntity.getCreatedAt())
                .addressLine(orderEntity.getDeliveryAddressLine())
                .landmark(orderEntity.getDeliveryLandmark())
                .city(orderEntity.getDeliveryCity())
                .state(orderEntity.getDeliveryState())
                .zipCode(orderEntity.getDeliveryZipCode())
                .country(orderEntity.getDeliveryCountry())
                .orderItems(items)
                .subTotal(orderEntity.getSubTotal())
                .discountAmount(orderEntity.getDiscountAmount())
                .deliveryCharges(orderEntity.getDeliveryCharges())
                .platformFees(orderEntity.getPlatformFees())
                .totalAmount(orderEntity.getTotalAmount())
                .build();
    }

    public static OrderResponseDTO toOrderResponseDTO(OrderEntity orderEntity) {
        return OrderResponseDTO.builder()
                .orderId(orderEntity.getOrderId())
                .userId(orderEntity.getUserId())
                .orderStatus(orderEntity.getOrderStatus())
                .totalAmount(orderEntity.getTotalAmount())
                .subTotal(orderEntity.getSubTotal())
                .discountAmount(orderEntity.getDiscountAmount())
                .platformFees(orderEntity.getPlatformFees())
                .deliveryCharges(orderEntity.getDeliveryCharges())
                .deliveryAddressLine(orderEntity.getDeliveryAddressLine())
                .deliveryLandmark(orderEntity.getDeliveryLandmark())
                .deliveryCity(orderEntity.getDeliveryCity())
                .deliveryState(orderEntity.getDeliveryState())
                .deliveryZipCode(orderEntity.getDeliveryZipCode())
                .deliveryCountry(orderEntity.getDeliveryCountry())
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .itemsResponseDTO(orderEntity.getOrderItems().stream().map(item ->
                        OrderItemResponseDTO.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .productPrice(item.getProductPrice())
                                .quantity(item.getQuantity())
                                .lineTotal(item.getLineTotal())
                                .imageUrl(item.getImageUrl())
                                .build()
                ).toList())
                .build();
    }

    public static NotificationEvent toOrderConfirmationNotificationEvent(OrderEvent orderEvent, OrderEntity orderEntity) {
        List<Item> items = new ArrayList<>();
        orderEntity.getOrderItems().forEach(orderItem -> items.add(getItem(orderItem)));
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderEvent.getOrderId());
        payload.put("totalAmount", orderEntity.getTotalAmount());
        payload.put("items", items);
        return NotificationEvent.builder()
                .notificationType(NotificationType.ORDER_COMPLETE)
                .userId(orderEvent.getUserId())
                .payload(payload)
                .build();
    }

    private static @NonNull Item getItem(OrderItemsEntity orderItem) {
        return new Item(
                orderItem.getProductName(),
                String.valueOf(orderItem.getProductPrice()),
                String.valueOf(orderItem.getQuantity())
        );
    }

    record Item(String name, String price, String quantity) {}
}
