package com.motocart.order_microservice.document.api.impl;

import com.motocart.library.common.annotation.MotocartAPI;
import com.motocart.library.common.dto.request.OrderSummaryRequestDTO;
import com.motocart.order_microservice.document.api.OrderDocumentResource;
import com.motocart.order_microservice.document.generator.OrderSummaryPdfGenerator;

@MotocartAPI("order/")
public class OrderDocumentResourceImpl implements OrderDocumentResource {

    private final OrderSummaryPdfGenerator orderSummaryPdfGenerator;

    public OrderDocumentResourceImpl(OrderSummaryPdfGenerator orderSummaryPdfGenerator) {
        this.orderSummaryPdfGenerator = orderSummaryPdfGenerator;
    }

    @Override
    public byte[] generateOrderSummaryPdf(OrderSummaryRequestDTO orderSummaryRequestDTO) {
        return orderSummaryPdfGenerator.generateOrderSummaryPdf(orderSummaryRequestDTO);
    }
}
