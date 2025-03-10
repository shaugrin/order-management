package com.example.inventory_service.controller;

import com.example.inventory_service.dto.StockUpdateRequest;
import com.example.inventory_service.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testReserveStock() throws Exception {
        StockUpdateRequest request = new StockUpdateRequest("SKU123", 10);

        // Mock service response
        when(inventoryService.reserveStock(eq("SKU123"), eq(10))).thenReturn(true);

        mockMvc.perform(post("/api/inventory/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verify service method was called
        Mockito.verify(inventoryService).reserveStock("SKU123", 10);
    }

    @Test
    void testReleaseStock() throws Exception {
        String sku = "SKU123";
        int quantity = 10;

        // Mock service behavior
        doNothing().when(inventoryService).releaseStock(eq(sku), eq(quantity));

        mockMvc.perform(post("/api/inventory/release")
                        .param("sku", sku)
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk());

        // Verify service method was called
        Mockito.verify(inventoryService).releaseStock(sku, quantity);
    }
}
