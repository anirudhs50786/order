package com.motocart.order_microservice.util;

import com.motocart.library.common.dto.CartDTO;
import com.motocart.library.common.dto.CartItemsDTO;
import com.motocart.library.common.dto.request.BillerItemDTO;
import com.motocart.library.common.dto.request.BillerRequestDTO;
import com.motocart.library.common.dto.response.BillerResponseDTO;
import com.motocart.library.common.event.CartEvent;
import com.motocart.library.common.event.InventoryEvent;
import com.motocart.library.common.event.OrderEvent;
import com.motocart.library.common.types.InventoryActionType;
import com.motocart.order_microservice.cart.entity.CartEntity;
import com.motocart.order_microservice.cart.entity.CartItemEntity;
import com.motocart.order_microservice.order.entity.OrderEntity;
import com.motocart.order_microservice.order.entity.OrderItemsEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
        return OrderEntity.builder()
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
    }

    private static List<OrderItemsEntity> toOrderItemList(List<CartItemEntity> cartItemEntities) {
        List<OrderItemsEntity> cartItems = new ArrayList<>();
        for(var item : cartItemEntities) {
            cartItems.add(OrderItemsEntity.builder()
                    .lineTotal(item.getQuantity() * item.getProductPrice())
                    .productName(item.getProductName())
                    .productPrice(item.getProductPrice())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .imageUrl(item.getImageUrl())
                    .build());
        }
        return cartItems;
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
}
