package com.ecommerce.order.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;
//    private Product product;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
}
