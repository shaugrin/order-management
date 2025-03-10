package com.example.product_service.service;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    ProductRepository repository;
    @InjectMocks
    ProductService productService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(repository.findAll()).thenReturn(List.of(new Product("id", "sku", "name", "description", new BigDecimal(0), "category")));

        List<Product> result = productService.getAllProducts();
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testGetProduct() throws Exception {
        when(repository.findById(anyString())).thenReturn(Optional.of(new Product("id", "sku", "name", "description", new BigDecimal(0), "category")));

        Optional<Product> result = productService.getProduct("id");
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product mockProduct = new Product("id", "sku", "name", "description", new BigDecimal(0), "category");
        when(repository.save(any(Product.class))).thenReturn(mockProduct);

        Product result = productService.createProduct(mockProduct);
        Assert.assertEquals(mockProduct, result);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product mockProduct = new Product("id", "sku", "name", "description", new BigDecimal(0), "category");
        when(repository.findById(anyString())).thenReturn(Optional.of(mockProduct));
        when(repository.save(any(Product.class))).thenReturn(mockProduct);

        Optional<Product> result = productService.updateProduct("id", mockProduct);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(repository.existsById(anyString())).thenReturn(true);

        boolean result = productService.deleteProduct("id");
        verify(repository).deleteById(anyString());
        Assert.assertTrue(result);
    }

    @Test
    public void testExistsBySku() throws Exception {
        when(repository.existsBySku(anyString())).thenReturn(true);

        boolean result = productService.existsBySku("sku");
        Assert.assertTrue(result);
    }
}
