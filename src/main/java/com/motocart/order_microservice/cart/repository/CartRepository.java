package com.motocart.order_microservice.cart.repository;

import com.motocart.order_microservice.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    Optional<CartEntity> findByUserId(int userId);
}
