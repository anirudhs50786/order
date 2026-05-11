package com.motocart.order_microservice.document.api;

import com.motocart.library.common.dto.request.OrderSummaryRequestDTO;
import com.motocart.order_microservice.document.vo.summary.OrderSummaryVO;

public interface OrderDocumentResource {

    byte[] generateOrderSummaryPdf(OrderSummaryRequestDTO orderSummaryRequestDTO);
}
