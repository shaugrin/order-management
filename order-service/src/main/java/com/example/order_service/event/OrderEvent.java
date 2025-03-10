package com.example.order_service.event;

import com.example.order_service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String orderId;
    private String sku;
    private int quantity;
    private OrderStatus status;
    private Instant timestamp = Instant.now();
    private String customerEmail;  // Added
    private String customerPhone;  // Added
}