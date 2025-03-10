package com.example.order_service.controller;

import com.example.common.NotiRequest;
import com.example.order_service.model.Order;
import com.example.order_service.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Given
        Order mockOrder = new Order();
        when(orderService.createOrder(any(NotiRequest.class))).thenReturn(mockOrder);

        // When
        Order result = orderController.createOrder(new NotiRequest("sku", 0, "customerEmail", "customerPhone"));

        // Then
        assertThat(result).isSameAs(mockOrder); // Ensures it's the same object
        verify(orderService).createOrder(any(NotiRequest.class)); // Ensures method is called
    }
}
