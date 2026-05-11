package com.motocart.order_microservice.document.vo.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAnalysisReportVO {

    private CoverPageVO coverPage;                      // Page 1
    private IntroPageVO introPage;                      // Page 3
    private PurchaseAnalysisVO purchaseAnalysis;        // Page 4
    private PaymentAnalysisVO paymentAnalysis;          // Page 5
    private DeliveryAnalysisVO deliveryAnalysis;        // Page 6
    private OrderStatusSummaryVO orderStatusSummary;    // Page 7
    private SpendingTrendVO spendingTrend;              // Page 8
}
