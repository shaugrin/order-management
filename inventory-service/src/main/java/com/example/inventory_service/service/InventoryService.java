package com.example.inventory_service.service;

import com.example.inventory_service.dto.StockUpdateRequest;
import com.example.inventory_service.exception.InventoryNotFoundException;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    @Transactional
    public boolean reserveStock(String productId, int quantity) {
        return repository.findById(productId)
                .map(inventory -> {
                    if (inventory.getQuantity() >= quantity) {
                        inventory.setQuantity(inventory.getQuantity() - quantity);
                        repository.save(inventory);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    // InventoryService.java
    @Transactional
    public void releaseStock(String sku, int quantity) {
        repository.findById(sku).ifPresent(inventory -> {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            repository.save(inventory);
        });
    }

    @Transactional
    public void deductStock(String productId, int quantity) {
        Inventory inventory = repository.findById(productId)
                .orElseThrow(InventoryNotFoundException::new);

        if (inventory.getQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for deduction");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        repository.save(inventory);
    }
}