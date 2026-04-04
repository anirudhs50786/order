package com.motocart.order_microservice.order.entity;

import com.motocart.library.common.types.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", updatable = false, nullable = false)
    private int orderId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "discount_amount", nullable = false)
    private double discountAmount;

    @Column(name = "platform_fees", nullable = false)
    private double platformFees;

    @Column(name = "sub_total", nullable = false)
    private double subTotal;

    @Column(name = "delivery_charges", nullable = false)
    private double deliveryCharges;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemsEntity> orderItems;

    @Column(name = "delivery_address_line")
    private String deliveryAddressLine;

    @Column(name = "delivery_landmark")
    private String deliveryLandmark;

    @Column(name = "delivery_city")
    private String deliveryCity;

    @Column(name = "delivery_state")
    private String deliveryState;

    @Column(name = "delivery_zip_code")
    private String deliveryZipCode;

    @Column(name = "delivery_country")
    private String deliveryCountry;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    //archive DB for old data?
}
