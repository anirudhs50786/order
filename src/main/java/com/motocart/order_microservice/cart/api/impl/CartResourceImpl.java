package com.motocart.order_microservice.cart.api.impl;

import com.motocart.library.common.dto.CartDTO;
import com.motocart.library.security.AuthHelper;
import com.motocart.order_microservice.cart.api.CartResource;
import com.motocart.order_microservice.cart.service.CartManagementService;
import com.motocart.order_microservice.util.Mapper;

public class CartResourceImpl implements CartResource {

    private final CartManagementService cartManagementService;

    public CartResourceImpl(CartManagementService cartManagementService) {
        this.cartManagementService = cartManagementService;
    }

    @Override
    public CartDTO getCartForLoggedInUser() {
        int userId = AuthHelper.getAuthUserId();
        return Mapper.toCartDTO(cartManagementService.getCartByUserId(userId));
    }
}
