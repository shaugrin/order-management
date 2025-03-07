package com.example.order_service.client;

import com.example.order_service.dto.ReserveStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryServiceClient {
    @PostMapping("/api/inventory/reserve")
    boolean reserveStock(ReserveStockRequest request);

    @PostMapping("/api/inventory/release")
    void releaseStock(@RequestParam("sku") String sku, @RequestParam("quantity") int quantity);
}