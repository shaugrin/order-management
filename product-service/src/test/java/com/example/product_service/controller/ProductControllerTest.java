package com.example.product_service.controller;

import com.example.product_service.model.Product;
import com.example.product_service.service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductControllerTest {
    @Mock
    ProductService service;
    @InjectMocks
    ProductController productController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(service.getAllProducts()).thenReturn(List.of(new Product("id", "sku", "name", "description", new BigDecimal(0), "category")));

        ResponseEntity<List<Product>> result = productController.getAllProducts();
        Assert.assertEquals(new ResponseEntity<List<Product>>(List.of(new Product("id", "sku", "name", "description", new BigDecimal(0), "category")), null, 200), result);
    }


    @Test
    public void testDeleteProduct() throws Exception {
        when(service.deleteProduct(anyString())).thenReturn(true);

        ResponseEntity<Void> result = productController.deleteProduct("id");
        Assert.assertEquals(new ResponseEntity<Void>(null, null, 204), result);
    }

    @Test
    public void testProductExistsBySku() throws Exception {
        when(service.existsBySku(anyString())).thenReturn(true);

        ResponseEntity<Boolean> result = productController.productExistsBySku("sku");
        Assert.assertEquals(new ResponseEntity<Boolean>(Boolean.TRUE, null, 200), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme