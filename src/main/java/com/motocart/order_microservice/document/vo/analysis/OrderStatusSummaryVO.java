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
public class OrderStatusSummaryVO {

    private int totalOrders;
    private int deliveredCount;
    private int cancelledCount;
    private int returnedCount;
    private int pendingCount;

    // percentage of orders cancelled out of total
    private double cancellationRate;

    // status -> count  (for chart rendering)
    private Map<String, Integer> statusBreakdown;
    private String statusPieChartBase64;            // pre-rendered by JFreeChart
}
