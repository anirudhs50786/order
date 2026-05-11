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
public class PaymentAnalysisVO {

    private double totalSpent;
    private double totalSaved;          // sum of discountAmount
    private double totalDeliveryCharges;
    private double totalPlatformFees;
    private double averageOrderValue;
    private double largestOrderValue;

    // payment method -> count  (e.g. "UPI" -> 5, "COD" -> 2)
    private Map<String, Integer> paymentMethodBreakdown;
    private String paymentMethodPieChartBase64;     // pre-rendered by JFreeChart
}
