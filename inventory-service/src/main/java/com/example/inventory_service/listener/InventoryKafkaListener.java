//package com.example.inventory_service.listener;
//
//import com.example.inventory_service.dto.StockUpdateRequest;
//import com.example.inventory_service.service.InventoryService;
//import com.example.inventory_service.dto.OrderEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class InventoryKafkaListener {
//    private final InventoryService inventoryService;
//
//    @KafkaListener(topics = "order-events", groupId = "inventory-group")
//    public void handleOrderEvent(OrderEvent event) {
//        if ("CONFIRMED".equals(event.getStatus())) {
//            inventoryService.deductStock(
//                    event.getProductId(),
//                    event.getQuantity()
//            );
//        }
//    }
//}