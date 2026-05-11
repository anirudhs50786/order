package com.motocart.order_microservice.document.generator;

import com.motocart.library.common.dto.request.OrderAnalysisRequestDTO;
import com.motocart.order_microservice.document.service.OrderAnalysisService;
import com.motocart.order_microservice.document.vo.analysis.OrderAnalysisReportVO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

public class OrderAnalysisPdfGenerator {

    private final OrderAnalysisService orderAnalysisService;
    private final TemplateEngine templateEngine;

    public OrderAnalysisPdfGenerator(OrderAnalysisService orderAnalysisService, TemplateEngine templateEngine) {
        this.orderAnalysisService = orderAnalysisService;
        this.templateEngine = templateEngine;
    }

    public byte[] generateOrderSummaryPdf(OrderAnalysisRequestDTO orderAnalysisRequestDTO) {
        OrderAnalysisReportVO analysisReportVO = orderAnalysisService.generateAnalysisData(orderAnalysisRequestDTO);
        Context context = new Context();
        context.setVariable("report", analysisReportVO);
        String html = templateEngine.process("order-analysis", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            new PdfRendererBuilder()
                    .withHtmlContent(html, null)
                    .toStream(outputStream)
                    .run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate order analysis report PDF", e);
        }
    }
}
