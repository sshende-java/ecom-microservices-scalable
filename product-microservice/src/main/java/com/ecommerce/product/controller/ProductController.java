package com.ecommerce.product.controller;


import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return new ResponseEntity<>(productService.createProduct(productRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO productRequestDTO) {

        Optional<ProductResponseDTO> optional = productService.updateProduct(id, productRequestDTO);

        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>("Product Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")      //http://localhost:8080/api/products/search?keyword=Galaxy
    public ResponseEntity<List<ProductResponseDTO>> searchProduct(@RequestParam String keyword) {
        List<ProductResponseDTO> responseDTO = productService.searchProduct(keyword);
        if (responseDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
