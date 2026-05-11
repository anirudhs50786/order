package com.motocart.order_microservice.document.vo.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OrderSummaryVO {

    private int orderId;
    private String orderStatus;
    private Instant createdAt;
    private String customerName;
    private String email;
    private String phone;

    private String addressLine;
    private String landmark;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private List<OrderItemVO> orderItems;

    private double subTotal;
    private double discountAmount;
    private double deliveryCharges;
    private double platformFees;
    private double totalAmount;

    private String paymentMethod;        // UPI / CARD / COD
    private String paymentStatus;        // SUCCESS / PENDING (COD)
    private String transactionId;        // null for COD
    private Instant paymentDate;         // null for COD
}
