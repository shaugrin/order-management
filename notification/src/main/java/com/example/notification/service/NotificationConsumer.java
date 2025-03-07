package com.example.notification.service;

import com.example.common.NotiRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    private static final Logger logger = LogManager.getLogger(NotificationConsumer.class);

    private final EmailService emailService;
    private final SmsService smsService;

    public NotificationConsumer(EmailService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void handleOrderEvent(NotiRequest event) {
        logger.info("Received OrderEvent: SKU={}, Quantity={}", event.sku(), event.quantity());

        try {
            // Send email
            emailService.sendEmail(
                    event.customerEmail(),
                    "Order Status Update",
                    "Your order " + event.sku() + " has quantity " + event.quantity()
            );
            logger.info("Email sent to {}", event.customerEmail());

            // Send SMS
            smsService.sendSms(
                    event.customerPhone(),
                    "Order " + event.sku() + " quantity: " + event.quantity()
            );
            logger.info("SMS sent to {}", event.customerPhone());

        } catch (Exception e) {
            logger.error("Error handling OrderEvent", e);
        }
    }
}
