package com.oms.inventory.service;

import com.oms.inventory.entity.Product;
import com.oms.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
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
