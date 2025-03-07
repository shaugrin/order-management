package com.example.notification.event;

import lombok.Data;
import java.time.Instant;

@Data
public class OrderEvent {
    private String orderId;
    private String sku;
    private int quantity;
    private String status;
    private Instant timestamp;
    private String customerEmail;
    private String customerPhone;
}