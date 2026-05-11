package com.motocart.order_microservice.document.vo.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseAnalysisVO {

    // Pie chart: category -> quantity bought
    private Map<String, Integer> categoryQuantityMap;
    private String categoryPieChartBase64;          // pre-rendered by JFreeChart

    // Bar graph: "MMM yyyy" -> items bought that month
    private Map<String, Integer> monthlyItemCountMap;
    private String monthlyBarChartBase64;           // pre-rendered by JFreeChart

    private String mostBoughtProduct;
    private List<String> top5Products;

    private int totalItemsBought;
    private int totalOrdersPlaced;
}
