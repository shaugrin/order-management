package com.example.product_service.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true) // Ensure SKU uniqueness
    private String sku; // New field

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private BigDecimal price;

    private String category;
}