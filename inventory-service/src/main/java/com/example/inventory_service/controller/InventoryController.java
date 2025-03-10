package com.example.inventory_service.controller;

import com.example.inventory_service.dto.StockUpdateRequest;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    public boolean reserveStock(@RequestBody StockUpdateRequest request) {
        return service.reserveStock(request.productId(), request.quantity());
    }

    @PostMapping("/release")
    @ResponseStatus(HttpStatus.OK)
    public void releaseStock(@RequestParam("sku") String sku, @RequestParam("quantity") int quantity) {
        service.releaseStock(sku, quantity);
    }
}