package com.motocart.order_microservice.cart.api;

import com.motocart.library.common.dto.CartDTO;

public interface CartResource {

    CartDTO getCartForLoggedInUser();
}
