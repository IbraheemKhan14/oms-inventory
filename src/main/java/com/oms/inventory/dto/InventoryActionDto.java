package com.oms.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryActionDto {
    @NotBlank(message = "Product code is required")
    private String productCode;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
