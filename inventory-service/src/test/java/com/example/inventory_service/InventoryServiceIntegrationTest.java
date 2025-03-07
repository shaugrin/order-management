//package com.example.inventory_service;
//
//import com.example.inventory_service.dto.StockUpdateRequest;
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
//public class InventoryServiceIntegrationTest {
//    @Container
//    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest");
//
//    @Container
//    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
//
//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
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
//    void shouldReserveStock() {
//        StockUpdateRequest request = new StockUpdateRequest("product-123", 5);
//
//        given()
//                .contentType("application/json")
//                .body(request)
//                .when()
//                .post("/api/inventory/reserve")
//                .then()
//                .statusCode(200)
//                .body(equalTo("true"));
//    }
//}