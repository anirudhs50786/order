package com.motocart.order_microservice.cart.service;

import com.motocart.library.common.event.CartEvent;
import com.motocart.order_microservice.cart.entity.CartEntity;
import com.motocart.order_microservice.cart.entity.CartItemEntity;
import com.motocart.order_microservice.cart.repository.CartRepository;
import com.motocart.order_microservice.util.Mapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CartManagementService {

    private final CartRepository cartRepository;

    public CartManagementService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addItemToCart(CartEvent cartEvent) {
        int userId = cartEvent.getUserId();
        CartEntity cartEntity;
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(userId);
        CartItemEntity cartItem = Mapper.toCartItemEntity(cartEvent);

        // update existing cart or create new cart
        if(cartOptional.isPresent()) {
            cartEntity = cartOptional.get();
            cartEntity.getCartItems().add(cartItem);
            cartEntity.setUpdatedAt(Instant.now());
        } else {
            cartEntity = CartEntity.builder()
                    .cartItems(List.of(cartItem))
                    .userId(cartEvent.getUserId())
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
        }

        cartRepository.save(cartEntity);
    }

    public CartEntity getCartByUserId(int userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }
}
