package com.oms.inventory.service;

import com.oms.inventory.entity.Product;
import com.oms.inventory.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryService {
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createOrUpdate(Product p) {
        Optional<Product> existingProduct = productRepository.findByProductCode(p.getProductCode());
        if (existingProduct.isPresent()) {
            Product e = existingProduct.get();
            e.setName(p.getName());
            e.setQuantity(p.getQuantity());
            e.setPrice(p.getPrice());
            return productRepository.save(e);
        }
        return productRepository.save(p);
    }

    public Map<String, Object> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Product> productPage = productRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("items", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return response;
    }

    public Optional<Product> getByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    @Transactional
    public boolean reserve(String productCode, int quantity) {
        return productRepository.reserve(productCode, quantity) == 1;
    }

    @Transactional
    public void release(String productCode, int quantity) {
        productRepository.release(productCode, quantity);
    }
}
