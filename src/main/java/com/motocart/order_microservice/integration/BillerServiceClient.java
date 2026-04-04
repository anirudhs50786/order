package com.motocart.order_microservice.integration;


import com.motocart.library.common.dto.request.BillerRequestDTO;
import com.motocart.library.common.dto.response.BillerResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;

public interface BillerServiceClient {

    @PostMapping
    BillerResponseDTO generateBill(BillerRequestDTO billerRequestDTO);
}
