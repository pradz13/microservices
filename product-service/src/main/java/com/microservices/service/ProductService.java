package com.microservices.service;

import com.microservices.dto.ProductRequest;
import com.microservices.dto.ProductResponse;
import com.microservices.model.Product;
import com.microservices.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        this.productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = this.productRepository.findAll();
        log.info("Retrieved {} products", productList.size());
        return productList
                .stream()
                .map(this::mapToProductResponse)
                .collect(toList());

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();

    }
}
