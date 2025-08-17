package com.ecommerce.order.controller;


import com.ecommerce.order.dto.OrderResponseDTO;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestHeader("X-User-ID") String userId) {

        Optional<OrderResponseDTO> orderResponseDTO = orderService.createOrder(userId);

        return orderResponseDTO.map(
                        order -> new ResponseEntity(order, HttpStatus.CREATED)).
                orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));
    }


//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//
//    }

}
