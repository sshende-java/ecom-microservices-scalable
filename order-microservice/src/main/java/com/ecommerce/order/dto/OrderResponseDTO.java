package com.ecommerce.order.dto;


import com.ecommerce.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<OrderItemDTO> items;

    private LocalDateTime createdAt;

}
