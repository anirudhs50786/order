package com.motocart.order_microservice.document.vo.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverPageVO {

    private String firmName;
    private String logoUrl;

    private String customerName;
    private String email;
    private String phone;

    private String documentTitle;
    private LocalDate timelineFrom;
    private LocalDate timelineTo;
    private Instant generatedAt;
}
