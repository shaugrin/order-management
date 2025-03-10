package com.example.inventory_service.integration;

import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.service.InventoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryServiceIT {

    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("inventory_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository repository;

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
    }

    @Test
    void testReserveStock_Success() {
        Inventory inventory = new Inventory();
        inventory.setSku("ABC123");
        inventory.setQuantity(10);
        repository.save(inventory);

        boolean result = inventoryService.reserveStock("ABC123", 5);

        assertTrue(result);
        Optional<Inventory> updatedInventory = repository.findById("ABC123");
        assertTrue(updatedInventory.isPresent());
        assertEquals(5, updatedInventory.get().getQuantity());
    }

    @Test
    void testReserveStock_NotEnoughStock() {
        Inventory inventory = new Inventory();
        inventory.setSku("ABC123");
        inventory.setQuantity(5);
        repository.save(inventory);

        boolean result = inventoryService.reserveStock("ABC123", 10);

        assertFalse(result);
        Optional<Inventory> updatedInventory = repository.findById("ABC123");
        assertTrue(updatedInventory.isPresent());
        assertEquals(5, updatedInventory.get().getQuantity());
    }

    @Test
    void testDeductStock_ThrowsExceptionWhenInsufficient() {
        Inventory inventory = new Inventory();
        inventory.setSku("ABC123");
        inventory.setQuantity(5);
        repository.save(inventory);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            inventoryService.deductStock("ABC123", 10);
        });

        assertEquals("Insufficient stock for deduction", exception.getMessage());
    }
}
