package com.example.order_service.dto;

public record ReserveStockRequest(String productId, int quantity) {}