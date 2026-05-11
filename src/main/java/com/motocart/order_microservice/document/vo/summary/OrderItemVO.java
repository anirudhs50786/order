package com.motocart.order_microservice.document.vo.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OrderItemVO {
    private String productName;
    private int quantity;
    private long productPrice;
    private long lineTotal;
    private String imageUrl;
}
