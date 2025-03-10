package com.example.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record NotiRequest(
        @NotBlank String sku,
        @Min(1) int quantity,
        @NotBlank @Email String customerEmail,  // Added
        @NotBlank String customerPhone          // Added
) {}