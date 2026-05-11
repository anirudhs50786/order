package com.motocart.order_microservice.document.service;

import com.motocart.library.common.dto.request.OrderSummaryRequestDTO;
import com.motocart.library.common.types.Roles;
import com.motocart.library.security.AuthHelper;
import com.motocart.order_microservice.document.vo.summary.OrderSummaryVO;
import com.motocart.order_microservice.order.entity.OrderEntity;
import com.motocart.order_microservice.order.repository.OrderRepository;
import com.motocart.order_microservice.util.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class OrderSummaryService {

    private final OrderRepository orderRepository;

    public OrderSummaryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderSummaryVO buildOrderSummaryData(OrderSummaryRequestDTO summaryRequestDTO) {
        validateRequest(summaryRequestDTO);
        int orderId = summaryRequestDTO.getOrderId();
        OrderEntity orderEntity = orderRepository.getReferenceById(orderId);
        OrderSummaryVO summaryVO = Mapper.toOrderSummaryVO(orderEntity);
        // fetch user profile info
        // fetch payment info
        return summaryVO;
    }

    private static void validateRequest(OrderSummaryRequestDTO summaryRequestDTO) {
        int loggedInUser = AuthHelper.getAuthUserId();
        boolean isAdmin = AuthHelper.getRoles().contains(Roles.ROLE_ADMIN.name());
        if (!isAdmin && loggedInUser != summaryRequestDTO.getUserId()) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
