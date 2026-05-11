package com.motocart.order_microservice.document.generator;

import com.motocart.library.common.dto.request.OrderSummaryRequestDTO;
import com.motocart.order_microservice.document.service.OrderSummaryService;
import com.motocart.order_microservice.document.vo.summary.OrderSummaryVO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Component
public class OrderSummaryPdfGenerator {

    private final TemplateEngine templateEngine;
    private final OrderSummaryService orderSummaryService;

    public OrderSummaryPdfGenerator(TemplateEngine templateEngine, OrderSummaryService orderSummaryService) {
        this.templateEngine = templateEngine;
        this.orderSummaryService = orderSummaryService;
    }

    public byte[] generateOrderSummaryPdf(OrderSummaryRequestDTO orderSummaryRequestDTO) {
        OrderSummaryVO orderSummaryVO = orderSummaryService.buildOrderSummaryData(orderSummaryRequestDTO);
        Context context = new Context();
        context.setVariable("order", orderSummaryVO);
        String html = templateEngine.process("order-summary", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            new PdfRendererBuilder()
                    .withHtmlContent(html, null)
                    .toStream(outputStream)
                    .run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate order summary PDF", e);
        }
    }
}
