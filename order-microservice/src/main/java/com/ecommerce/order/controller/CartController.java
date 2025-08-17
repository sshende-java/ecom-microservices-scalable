package com.ecommerce.order.controller;


import com.ecommerce.order.dto.CartItemRequestDTO;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequestDTO request) {

        boolean isAddedToCart = cartService.addToCart(userId, request);
        if (!isAddedToCart) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found OR Out of Stock OR User Not Found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Cart Item added successfully");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String  productId) {

        Boolean isDeleted = cartService.deleteItemFromCart(userId, productId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }


    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId) {

        List<CartItem> cartItem = cartService.getCart(userId);
        if (cartItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartItem);
    }


}
