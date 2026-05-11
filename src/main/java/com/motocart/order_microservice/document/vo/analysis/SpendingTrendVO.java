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
public class SpendingTrendVO {

    // "MMM yyyy" -> total amount spent that month
    private Map<String, Double> monthlySpendMap;
    private String spendingLineChartBase64;         // pre-rendered by JFreeChart

    private String highestSpendMonth;
    private double highestSpendAmount;

    private String lowestSpendMonth;
    private double lowestSpendAmount;

    private double averageMonthlySpend;
}
