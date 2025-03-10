package com.example.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    // Removed email/phone fields (now handled by OrderEvent)
}