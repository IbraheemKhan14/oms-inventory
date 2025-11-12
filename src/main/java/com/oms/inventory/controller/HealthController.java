package com.oms.inventory.controller;

import com.oms.inventory.common.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(ApiResponse.ok(
                "Inventory service is up and running. Timestamp: " + Instant.now()
        ));
    }
}
