package com.example.inventory_service.dto;

public record StockUpdateRequest(String productId, int quantity) {
}