package com.example.order_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank String sku,
        @Min(1) int quantity,
        @NotBlank @Email String customerEmail,  // Added
        @NotBlank String customerPhone          // Added
) {}