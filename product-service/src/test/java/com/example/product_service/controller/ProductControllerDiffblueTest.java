//package com.example.product_service.controller;
//
//import static org.mockito.Mockito.when;
//
//import com.example.product_service.model.Product;
//import com.example.product_service.service.ProductService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Optional;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.aot.DisabledInAotMode;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ContextConfiguration(classes = {ProductController.class})
//@ExtendWith(SpringExtension.class)
//@DisabledInAotMode
//class ProductControllerDiffblueTest {
//    @Autowired
//    private ProductController productController;
//
//    @MockBean
//    private ProductService productService;
//
//    /**
//     * Test {@link ProductController#getAllProducts()}.
//     * <p>
//     * Method under test: {@link ProductController#getAllProducts()}
//     */
//    @Test
//    @DisplayName("Test getAllProducts()")
//    @Tag("MaintainedByDiffblue")
//    void testGetAllProducts() throws Exception {
//        // Arrange
//        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//
//    /**
//     * Test {@link ProductController#getProduct(String)}.
//     * <p>
//     * Method under test: {@link ProductController#getProduct(String)}
//     */
//    @Test
//    @DisplayName("Test getProduct(String)")
//    @Tag("MaintainedByDiffblue")
//    void testGetProduct() throws Exception {
//        // Arrange
//        Product product = new Product();
//        product.setCategory("Category");
//        product.setDescription("The characteristics of someone or something");
//        product.setId("42");
//        product.setName("Name");
//        product.setPrice(new BigDecimal("2.3"));
//        Optional<Product> ofResult = Optional.of(product);
//        when(productService.getProduct(Mockito.<String>any())).thenReturn(ofResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/{id}", "42");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"id\":\"42\",\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"price\":2.3,"
//                                        + "\"category\":\"Category\"}"));
//    }
//
//    /**
//     * Test {@link ProductController#createProduct(Product)}.
//     * <p>
//     * Method under test: {@link ProductController#createProduct(Product)}
//     */
//    @Test
//    @DisplayName("Test createProduct(Product)")
//    @Tag("MaintainedByDiffblue")
//    void testCreateProduct() throws Exception {
//        // Arrange
//        Product product = new Product();
//        product.setCategory("Category");
//        product.setDescription("The characteristics of someone or something");
//        product.setId("42");
//        product.setName("Name");
//        product.setPrice(new BigDecimal("2.3"));
//        when(productService.createProduct(Mockito.<Product>any())).thenReturn(product);
//
//        Product product2 = new Product();
//        product2.setCategory("Category");
//        product2.setDescription("The characteristics of someone or something");
//        product2.setId("42");
//        product2.setName("Name");
//        product2.setPrice(new BigDecimal("2.3"));
//        String content = (new ObjectMapper()).writeValueAsString(product2);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/products")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"id\":\"42\",\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"price\":2.3,"
//                                        + "\"category\":\"Category\"}"))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/products/42"));
//    }
//
//    /**
//     * Test {@link ProductController#updateProduct(String, Product)}.
//     * <p>
//     * Method under test: {@link ProductController#updateProduct(String, Product)}
//     */
//    @Test
//    @DisplayName("Test updateProduct(String, Product)")
//    @Tag("MaintainedByDiffblue")
//    void testUpdateProduct() throws Exception {
//        // Arrange
//        Product product = new Product();
//        product.setCategory("Category");
//        product.setDescription("The characteristics of someone or something");
//        product.setId("42");
//        product.setName("Name");
//        product.setPrice(new BigDecimal("2.3"));
//        Optional<Product> ofResult = Optional.of(product);
//        when(productService.updateProduct(Mockito.<String>any(), Mockito.<Product>any())).thenReturn(ofResult);
//
//        Product product2 = new Product();
//        product2.setCategory("Category");
//        product2.setDescription("The characteristics of someone or something");
//        product2.setId("42");
//        product2.setName("Name");
//        product2.setPrice(new BigDecimal("2.3"));
//        String content = (new ObjectMapper()).writeValueAsString(product2);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/products/{id}", "42")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"id\":\"42\",\"name\":\"Name\",\"description\":\"The characteristics of someone or something\",\"price\":2.3,"
//                                        + "\"category\":\"Category\"}"));
//    }
//
//    /**
//     * Test {@link ProductController#deleteProduct(String)}.
//     * <ul>
//     *   <li>Then status {@link StatusResultMatchers#isNoContent()}.</li>
//     * </ul>
//     * <p>
//     * Method under test: {@link ProductController#deleteProduct(String)}
//     */
//    @Test
//    @DisplayName("Test deleteProduct(String); then status isNoContent()")
//    @Tag("MaintainedByDiffblue")
//    void testDeleteProduct_thenStatusIsNoContent() throws Exception {
//        // Arrange
//        when(productService.deleteProduct(Mockito.<String>any())).thenReturn(true);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/products/{id}", "42");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//
//    /**
//     * Test {@link ProductController#deleteProduct(String)}.
//     * <ul>
//     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
//     * </ul>
//     * <p>
//     * Method under test: {@link ProductController#deleteProduct(String)}
//     */
//    @Test
//    @DisplayName("Test deleteProduct(String); then status isNotFound()")
//    @Tag("MaintainedByDiffblue")
//    void testDeleteProduct_thenStatusIsNotFound() throws Exception {
//        // Arrange
//        when(productService.deleteProduct(Mockito.<String>any())).thenReturn(false);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/products/{id}", "42");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(productController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//}
