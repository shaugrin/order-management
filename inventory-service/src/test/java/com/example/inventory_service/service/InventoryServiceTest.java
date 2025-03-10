package com.example.inventory_service.service;

import com.example.inventory_service.exception.InventoryNotFoundException;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        sampleInventory = new Inventory();
        sampleInventory.setSku("ABC123");
        sampleInventory.setQuantity(10);
    }

    @Test
    void testReserveStock_Success() {
        when(repository.findById("ABC123")).thenReturn(Optional.of(sampleInventory));

        boolean result = inventoryService.reserveStock("ABC123", 5);

        assertTrue(result);
        assertEquals(5, sampleInventory.getQuantity());
        verify(repository, times(1)).save(sampleInventory);
    }

    @Test
    void testReserveStock_NotEnoughStock() {
        when(repository.findById("ABC123")).thenReturn(Optional.of(sampleInventory));

        boolean result = inventoryService.reserveStock("ABC123", 15);

        assertFalse(result);
        assertEquals(10, sampleInventory.getQuantity());
        verify(repository, never()).save(any());
    }

    @Test
    void testReserveStock_ProductNotFound() {
        when(repository.findById("XYZ999")).thenReturn(Optional.empty());

        boolean result = inventoryService.reserveStock("XYZ999", 5);

        assertFalse(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testReleaseStock_Success() {
        when(repository.findById("ABC123")).thenReturn(Optional.of(sampleInventory));

        inventoryService.releaseStock("ABC123", 3);

        assertEquals(13, sampleInventory.getQuantity());
        verify(repository, times(1)).save(sampleInventory);
    }

    @Test
    void testReleaseStock_ProductNotFound() {
        when(repository.findById("XYZ999")).thenReturn(Optional.empty());

        inventoryService.releaseStock("XYZ999", 3);

        verify(repository, never()).save(any());
    }

    @Test
    void testDeductStock_Success() {
        when(repository.findById("ABC123")).thenReturn(Optional.of(sampleInventory));

        inventoryService.deductStock("ABC123", 5);

        assertEquals(5, sampleInventory.getQuantity());
        verify(repository, times(1)).save(sampleInventory);
    }

    @Test
    void testDeductStock_NotEnoughStock() {
        when(repository.findById("ABC123")).thenReturn(Optional.of(sampleInventory));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                inventoryService.deductStock("ABC123", 15)
        );

        assertEquals("Insufficient stock for deduction", exception.getMessage());
        assertEquals(10, sampleInventory.getQuantity());
        verify(repository, never()).save(any());
    }

    @Test
    void testDeductStock_ProductNotFound() {
        when(repository.findById("XYZ999")).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () ->
                inventoryService.deductStock("XYZ999", 5)
        );

        verify(repository, never()).save(any());
    }
}
