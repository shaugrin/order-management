package com.example.inventory_service.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException() {
        super("Inventory record not found");
    }
}