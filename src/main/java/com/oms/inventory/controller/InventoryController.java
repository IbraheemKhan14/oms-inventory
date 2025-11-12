package com.oms.inventory.controller;

import com.oms.inventory.common.utils.ApiErrorResponse;
import com.oms.inventory.common.utils.ApiResponse;
import com.oms.inventory.dto.InventoryActionDto;
import com.oms.inventory.dto.ProductDto;
import com.oms.inventory.entity.Product;
import com.oms.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/products")
    public ResponseEntity<?> createOrUpdate(@Valid @RequestBody ProductDto req) {
        try {
            Product product = Product.builder()
                    .productCode(req.getProductCode())
                    .name(req.getName())
                    .quantity(req.getQuantity())
                    .price(req.getPrice())
                    .build();

            Product saved = inventoryService.createOrUpdate(product);

            return ResponseEntity.ok(
                    ApiResponse.ok(saved)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.of("PRODUCT_SAVE_FAILED", ex.getMessage())
            );
        }
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(inventoryService.getAllProducts())
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.of("FETCH_FAILED", ex.getMessage())
            );
        }
    }

    @GetMapping("/products/{productCode}")
    public ResponseEntity<?> get(@PathVariable String productCode) {
        return inventoryService.getByProductCode(productCode)
                .<ResponseEntity<?>>map(product ->
                        ResponseEntity.ok(ApiResponse.ok(product))
                )
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ApiErrorResponse.of("PRODUCT_NOT_FOUND",
                                "No product found with code: " + productCode)
                ));
    }

    @PostMapping("/reserve")
    @Transactional
    public ResponseEntity<?> reserve(@Valid @RequestBody InventoryActionDto req) {
        try {
            boolean reserved = inventoryService.reserve(req.getProductCode(), req.getQuantity());

            if (reserved) {
                return ResponseEntity.ok(ApiResponse.ok(
                        String.format("Reserved %d unit(s) of %s successfully.",
                                req.getQuantity(), req.getProductCode())
                ));
            }

            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiErrorResponse.of("INSUFFICIENT_STOCK",
                            String.format("Not enough stock available for product '%s'. Requested: %d",
                                    req.getProductCode(), req.getQuantity()))
            );

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.of("RESERVE_FAILED", ex.getMessage())
            );
        }
    }

    @PostMapping("/release")
    public ResponseEntity<?> release(@Valid @RequestBody InventoryActionDto req) {
        try {
            inventoryService.release(req.getProductCode(), req.getQuantity());
            return ResponseEntity.ok(ApiResponse.ok(String.format("Released %d unit(s) of %s back to inventory.", req.getQuantity(), req.getProductCode())));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.of("RELEASE_FAILED", ex.getMessage()));
        }
    }
}
