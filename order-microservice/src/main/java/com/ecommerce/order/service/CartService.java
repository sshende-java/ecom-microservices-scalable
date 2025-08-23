package com.ecommerce.order.service;


import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequestDTO;
import com.ecommerce.order.dto.ProductResponseDTO;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {


    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;


    public boolean addToCart(String userId, CartItemRequestDTO request) {

        //Validate User
        UserResponse userApiResponse = userServiceClient.getUserById(userId);
        //If user not found
        if (userApiResponse == null) {
            return false;
        }

        //Look for Product
        ProductResponseDTO productApiResponse = productServiceClient.getProductById(request.getProductId());
        //If prod not found
        if (productApiResponse == null) {
            return false;
        }

        //if insufficient quantity return
        if (productApiResponse.getStockQuantity() < request.getQuantity()) {
            return false;
        }

        //Add to cart
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            //If cart already exists Update the cart with quantity and price
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else {
            //Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    @Transactional
    public Boolean deleteItemFromCart(String userId, String productId) {
        long deleteCount = 0;

//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        //If user not found
//        if (userOptional.isEmpty()) {
//            return false;
//        }
//
//        Optional<Product> productOptional = productRepository.findById(productId);
//        //If product not found
//        if (productOptional.isEmpty()) {
//            return false;
//        }
//
//        Product product = productOptional.get();
//        User user = userOptional.get();
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;

    }

    public List<CartItem> getCart(String userId) {
//       return userRepository.findById(Long.valueOf(userId))
//                .map(user-> cartItemRepository.findByUser(user)).orElse(Collections.emptyList());
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isPresent()) {
//            cartItemRepository.deleteByUser(userOptional.get());
//        }
        cartItemRepository.deleteByUserId(userId);
    }
}
