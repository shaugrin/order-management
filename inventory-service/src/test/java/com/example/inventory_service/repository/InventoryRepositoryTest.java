package com.example.inventory_service.repository;

import com.example.inventory_service.model.Inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest // Loads only JPA-related components for testing
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void testSaveAndFindById() {
        // Given: Creating an inventory entity
        Inventory inventory = new Inventory();
        inventory.setSku("SKU123");
        inventory.setQuantity(50);

        // When: Saving inventory to DB
        inventoryRepository.save(inventory);

        // Then: Fetch and verify
        Optional<Inventory> foundInventory = inventoryRepository.findById("SKU123");
        assertThat(foundInventory).isPresent();
        assertThat(foundInventory.get().getSku()).isEqualTo("SKU123");
        assertThat(foundInventory.get().getQuantity()).isEqualTo(50);
    }

    @Test
    void testDeleteInventory() {
        // Given
        Inventory inventory = new Inventory();
        inventory.setSku("SKU123");
        inventory.setQuantity(50);
        inventoryRepository.save(inventory);

        // When: Deleting entity
        inventoryRepository.deleteById("SKU123");

        // Then: Ensure it's removed
        Optional<Inventory> foundInventory = inventoryRepository.findById("SKU123");
        assertThat(foundInventory).isEmpty();
    }
}
