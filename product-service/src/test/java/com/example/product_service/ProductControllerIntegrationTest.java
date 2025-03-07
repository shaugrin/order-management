//package com.example.product_service;
//
//import com.example.product_service.model.Product;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
//public class ProductControllerIntegrationTest {
//    @Container
//    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
//
//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//    }
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    void setup() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    void shouldCreateAndRetrieveProduct() {
//        Product testProduct = new Product(null, "Laptop", "High-performance",
//                BigDecimal.valueOf(999.99), "Electronics");
//
//        String location = given()
//                .contentType(ContentType.JSON)
//                .body(testProduct)
//                .when()
//                .post("/api/products")
//                .then()
//                .statusCode(201)
//                .extract().header("Location");
//
//        given()
//                .when()
//                .get(location)
//                .then()
//                .statusCode(200)
//                .body("name", equalTo("Laptop"))
//                .body("price", equalTo(999.99f));
//    }
//
//    @Test
//    void shouldRejectNegativePrice() {
//        Product invalidProduct = new Product(null, "Test", "Invalid",
//                BigDecimal.valueOf(-10), "Test");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(invalidProduct)
//                .when()
//                .post("/api/products")
//                .then()
//                .statusCode(400);
//    }
//}