package com.example.inventory_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @Column(nullable = false)
    private String sku; // Now uses SKU instead of MongoDB ID

    @Column(nullable = false)
    private Integer quantity;
}