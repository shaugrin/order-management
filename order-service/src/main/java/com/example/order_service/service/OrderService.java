package com.example.order_service.service;

import com.example.order_service.client.InventoryServiceClient;
import com.example.order_service.client.ProductServiceClient;
import com.example.common.NotiRequest;
import com.example.order_service.exception.InsufficientStockException;
import com.example.order_service.exception.ProductNotFoundException;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderStatus;
import com.example.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import com.example.order_service.dto.ReserveStockRequest;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, NotiRequest> kafkaTemplate;

    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public Order createOrder(NotiRequest request) {
        Order order = new Order();
        try {
            // Validate product exists
            if (!productServiceClient.productExistsBySku(request.sku())) {
                throw new ProductNotFoundException();
            }

            // Reserve stock
            boolean stockReserved = inventoryServiceClient.reserveStock(
                    new ReserveStockRequest(request.sku(), request.quantity())
            );
            if (!stockReserved) {
                throw new InsufficientStockException();
            }

            // Create order with customer details
            order.setId(UUID.randomUUID().toString());
            order.setSku(request.sku());
            order.setQuantity(request.quantity());
            order.setCustomerEmail(request.customerEmail());  // Set email
            order.setCustomerPhone(request.customerPhone());  // Set phone
            order.setStatus(OrderStatus.CREATED);

            Order savedOrder = orderRepository.save(order);

            // Publish Kafka event with all details
            kafkaTemplate.send("order-events",
                    new NotiRequest(
                            savedOrder.getSku(),
                            savedOrder.getQuantity(),
                            savedOrder.getCustomerEmail(),  // Added
                            savedOrder.getCustomerPhone()   // Added
                    )
            );

            // Update status to CONFIRMED after successful processing
            savedOrder.setStatus(OrderStatus.CONFIRMED);
            return savedOrder;
        } catch (Exception ex) {
            order.setStatus(OrderStatus.FAILED);
            inventoryServiceClient.releaseStock(request.sku(), request.quantity());
            throw ex;
        }
    }


    // Fallback method
    private Order fallbackCreateOrder(NotiRequest request, Throwable t) {
        throw new RuntimeException("Order creation failed: " + t.getMessage());
    }
}