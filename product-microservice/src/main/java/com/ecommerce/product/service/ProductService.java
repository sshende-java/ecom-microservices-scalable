package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequestDTO;
import com.ecommerce.product.dto.ProductResponseDTO;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //create product
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        mapProductRequestDTOtoProduct(product, productRequestDTO);      //DTO to product
        Product savedProduct = productRepository.save(product);
        return mapProductToProductResponseDTO(savedProduct);            //product to DTO
    }

    private ProductResponseDTO mapProductToProductResponseDTO(Product savedProduct) {
        ProductResponseDTO response = new ProductResponseDTO();

        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setStockQuantity(savedProduct.getStockQuantity());
        response.setCategory(savedProduct.getCategory());
        response.setActive(savedProduct.getActive());
        response.setImageUrl(savedProduct.getImageUrl());

        return response;

    }

    private void mapProductRequestDTOtoProduct(Product product, ProductRequestDTO requestDTO) {
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStockQuantity(requestDTO.getStockQuantity());
        product.setCategory(requestDTO.getCategory());
        product.setImageUrl(requestDTO.getImageUrl());
    }


    public Optional<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        return productRepository.findById(id).map(existing -> {
            mapProductRequestDTOtoProduct(existing, productRequestDTO);     //transform request to product
            productRepository.save(existing);
            return mapProductToProductResponseDTO(existing);        //prepare response DTO
        });
    }


    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findByActiveTrue().stream().map(this::mapProductToProductResponseDTO).toList();
    }


    //Not deleting product just marking it as INACTIVE
    public Boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setActive(false);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    public List<ProductResponseDTO> searchProduct(String keyword) {
        return productRepository.searchProduct(keyword).stream().map(this::mapProductToProductResponseDTO).toList();
    }
}
