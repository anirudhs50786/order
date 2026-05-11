package com.motocart.order_microservice.document.vo.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAnalysisVO {

    private String mostOrderedToCity;
    private String mostOrderedToState;

    // city -> order count
    private Map<String, Integer> cityOrderCountMap;

    private double totalDeliveryChargesPaid;
    private int freeDeliveryOrderCount;
}
