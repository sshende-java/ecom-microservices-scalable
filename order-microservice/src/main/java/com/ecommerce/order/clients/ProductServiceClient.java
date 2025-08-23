package com.ecommerce.order.clients;

import com.ecommerce.order.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable String id);

}
