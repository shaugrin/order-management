package com.example.api_gateway;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Value;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.gateway.discovery.locator.enabled=false",
                "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://dummy-issuer"
        }
)
@AutoConfigureWireMock(port = 0)
class ApiGatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RouteLocator testRoutes(RouteLocatorBuilder builder,
                                       @Value("${wiremock.server.port}") int wiremockPort) {
            return builder.routes()
                    .route("order-service", r -> r.path("/api/orders/**")
                            .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                            .uri("http://localhost:" + wiremockPort))
                    .route("inventory-service", r -> r.path("/api/inventory/**")
                            .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                            .uri("http://localhost:" + wiremockPort))
                    .route("product-service", r -> r.path("/api/products/**")
                            .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                            .uri("http://localhost:" + wiremockPort))
                    .build();
        }
    }

    @Test
    void shouldRouteToProductService() {
        // Stub rewritten path /products
        stubFor(get(urlPathEqualTo("/products"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                [
                    {
                        "id": "67c70e0eda16f263cdd9a333",
                        "sku": "ID1",
                        "name": "LEGO set",
                        "description": "This is a new product",
                        "price": 19.99,
                        "category": "Toys"
                    },
                    {
                        "id": "67c71d0c4f7a73137984a753",
                        "sku": "ID2",
                        "name": "LEGO Batman set",
                        "description": "This is a new product",
                        "price": 19.99,
                        "category": "Toys"
                    }
                ]
                """)));

        webTestClient.get()
                .uri("/api/products")
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4NVZhTnVXWkpWN05xaXR2Vy0zMmlYVG1rdmRjTTFibm9vTld0SnFnSW9nIn0.eyJleHAiOjE3NDE1NDA2MjYsImlhdCI6MTc0MTUyMjYyNiwianRpIjoiZDJkYWIzNjgtMmJiYy00ZDQ3LTgyOWMtMTg5YmRmZmJlMzhjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDkwL3JlYWxtcy95b3VyLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImE2MTI0ZmJjLTJlNzAtNGI1OC05ZjFjLTIwMmFiMDEyMDc3YyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS1nYXRld2F5LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YTg4YzlhYi1mZWYxLTQ1MDYtYTExMC1mMWNlN2JjNjQzOGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMteW91ci1yZWFsbSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhcGktZ2F0ZXdheS1jbGllbnQiOnsicm9sZXMiOlsiUk9MRV9BRE1JTiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjZhODhjOWFiLWZlZjEtNDUwNi1hMTEwLWYxY2U3YmM2NDM4YyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiU3VuaWwgS3VtYXIgSyIsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3RhZG1pbiIsImdpdmVuX25hbWUiOiJTdW5pbCIsImZhbWlseV9uYW1lIjoiS3VtYXIgSyIsImVtYWlsIjoic3VuaWxrb3R0b2RpQGdtYWlsLmNvbSJ9.h-zGvfIFeoy8etNivO0DICGFIUye2WWMTbyyxrYMNSJJl70XV7cc3SBS5KazEHgp1CyNJR0Q6Ri9_cJ9d5gqTByW_NwsDnM7PAgna2vMPgyZIp8f0tuVkuBPyeNbx3025LK9awH-wsAPfR11yrd4_fm6vZS9TkOx_V8IXxMnyWgoTfPHerCeyp6ViFtQHYKr24jfqk9hSVr9CGo7QtTSTQwztcOmTVebh7NCZu_8QWvysZy0T_dkA1WV1-e93kq0mUpQS4i4bURuFxGdv2D3hmuqQqNzh0Wa4gMcNCWE6yTYGH9T3ycHQRfsdCEPLXThMseQZx2xnSgvFTGSaIU4KA")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("67c70e0eda16f263cdd9a333")
                .jsonPath("$[0].sku").isEqualTo("ID1")
                .jsonPath("$[1].name").isEqualTo("LEGO Batman set")
                .jsonPath("$.length()").isEqualTo(2);
    }

    @Test
    void shouldRouteToOrderService() {
        // Stub for rewritten path /orders
        stubFor(post(urlPathEqualTo("/orders"))
                .withRequestBody(equalToJson("""
                {
                    "sku": "ID2",
                    "quantity": 1,
                    "customerEmail": "digvijaysunil@gmail.com",
                    "customerPhone": "+919116669942"
                }
                """))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                    {
                        "id": "86997dce-09ff-44f1-b9a5-31d9c603513d",
                        "sku": "ID2",
                        "quantity": 1,
                        "status": "CONFIRMED",
                        "orderDate": "2025-03-09T05:58:34.206065900Z",
                        "customerEmail": "digvijaysunil@gmail.com",
                        "customerPhone": "+919116669942"
                    }
                    """)));

        webTestClient.post()
                .uri("/api/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4NVZhTnVXWkpWN05xaXR2Vy0zMmlYVG1rdmRjTTFibm9vTld0SnFnSW9nIn0.eyJleHAiOjE3NDE1NDA2MjYsImlhdCI6MTc0MTUyMjYyNiwianRpIjoiZDJkYWIzNjgtMmJiYy00ZDQ3LTgyOWMtMTg5YmRmZmJlMzhjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDkwL3JlYWxtcy95b3VyLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImE2MTI0ZmJjLTJlNzAtNGI1OC05ZjFjLTIwMmFiMDEyMDc3YyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS1nYXRld2F5LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YTg4YzlhYi1mZWYxLTQ1MDYtYTExMC1mMWNlN2JjNjQzOGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMteW91ci1yZWFsbSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhcGktZ2F0ZXdheS1jbGllbnQiOnsicm9sZXMiOlsiUk9MRV9BRE1JTiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjZhODhjOWFiLWZlZjEtNDUwNi1hMTEwLWYxY2U3YmM2NDM4YyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiU3VuaWwgS3VtYXIgSyIsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3RhZG1pbiIsImdpdmVuX25hbWUiOiJTdW5pbCIsImZhbWlseV9uYW1lIjoiS3VtYXIgSyIsImVtYWlsIjoic3VuaWxrb3R0b2RpQGdtYWlsLmNvbSJ9.h-zGvfIFeoy8etNivO0DICGFIUye2WWMTbyyxrYMNSJJl70XV7cc3SBS5KazEHgp1CyNJR0Q6Ri9_cJ9d5gqTByW_NwsDnM7PAgna2vMPgyZIp8f0tuVkuBPyeNbx3025LK9awH-wsAPfR11yrd4_fm6vZS9TkOx_V8IXxMnyWgoTfPHerCeyp6ViFtQHYKr24jfqk9hSVr9CGo7QtTSTQwztcOmTVebh7NCZu_8QWvysZy0T_dkA1WV1-e93kq0mUpQS4i4bURuFxGdv2D3hmuqQqNzh0Wa4gMcNCWE6yTYGH9T3ycHQRfsdCEPLXThMseQZx2xnSgvFTGSaIU4KA")
                .bodyValue("""
                {
                    "sku": "ID2",
                    "quantity": 1,
                    "customerEmail": "digvijaysunil@gmail.com",
                    "customerPhone": "+919116669942"
                }
                """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("86997dce-09ff-44f1-b9a5-31d9c603513d")
                .jsonPath("$.status").isEqualTo("CONFIRMED")
                .jsonPath("$.orderDate").isEqualTo("2025-03-09T05:58:34.206065900Z");
    }

    @Test
    void shouldReserveInventory() {
        // Stub for rewritten path /inventory/reserve
        stubFor(post(urlPathEqualTo("/inventory/reserve"))
                .withRequestBody(equalToJson("""
                {
                    "productId": "ID2",
                    "quantity": 1
                }
                """))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("true")));

        webTestClient.post()
                .uri("/api/inventory/reserve")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4NVZhTnVXWkpWN05xaXR2Vy0zMmlYVG1rdmRjTTFibm9vTld0SnFnSW9nIn0.eyJleHAiOjE3NDE1NDA2MjYsImlhdCI6MTc0MTUyMjYyNiwianRpIjoiZDJkYWIzNjgtMmJiYy00ZDQ3LTgyOWMtMTg5YmRmZmJlMzhjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDkwL3JlYWxtcy95b3VyLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImE2MTI0ZmJjLTJlNzAtNGI1OC05ZjFjLTIwMmFiMDEyMDc3YyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS1nYXRld2F5LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YTg4YzlhYi1mZWYxLTQ1MDYtYTExMC1mMWNlN2JjNjQzOGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMteW91ci1yZWFsbSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhcGktZ2F0ZXdheS1jbGllbnQiOnsicm9sZXMiOlsiUk9MRV9BRE1JTiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjZhODhjOWFiLWZlZjEtNDUwNi1hMTEwLWYxY2U3YmM2NDM4YyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiU3VuaWwgS3VtYXIgSyIsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3RhZG1pbiIsImdpdmVuX25hbWUiOiJTdW5pbCIsImZhbWlseV9uYW1lIjoiS3VtYXIgSyIsImVtYWlsIjoic3VuaWxrb3R0b2RpQGdtYWlsLmNvbSJ9.h-zGvfIFeoy8etNivO0DICGFIUye2WWMTbyyxrYMNSJJl70XV7cc3SBS5KazEHgp1CyNJR0Q6Ri9_cJ9d5gqTByW_NwsDnM7PAgna2vMPgyZIp8f0tuVkuBPyeNbx3025LK9awH-wsAPfR11yrd4_fm6vZS9TkOx_V8IXxMnyWgoTfPHerCeyp6ViFtQHYKr24jfqk9hSVr9CGo7QtTSTQwztcOmTVebh7NCZu_8QWvysZy0T_dkA1WV1-e93kq0mUpQS4i4bURuFxGdv2D3hmuqQqNzh0Wa4gMcNCWE6yTYGH9T3ycHQRfsdCEPLXThMseQZx2xnSgvFTGSaIU4KA")
                .bodyValue("""
                {
                    "productId": "ID2",
                    "quantity": 1
                }
                """)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

//    @Test
//    void shouldReturnFallbackWhenServiceUnavailable() {
//        // Stub with delay longer than timeout
//        stubFor(post(urlPathEqualTo("/orders"))
//                .willReturn(aResponse()
//                        .withFixedDelay(2500) // Longer than 1s timeout
//                        .withStatus(200)));
//
//        webTestClient.post()
//                .uri("/api/orders")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4NVZhTnVXWkpWN05xaXR2Vy0zMmlYVG1rdmRjTTFibm9vTld0SnFnSW9nIn0.eyJleHAiOjE3NDE1NDA2MjYsImlhdCI6MTc0MTUyMjYyNiwianRpIjoiZDJkYWIzNjgtMmJiYy00ZDQ3LTgyOWMtMTg5YmRmZmJlMzhjIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDkwL3JlYWxtcy95b3VyLXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImE2MTI0ZmJjLTJlNzAtNGI1OC05ZjFjLTIwMmFiMDEyMDc3YyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS1nYXRld2F5LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI2YTg4YzlhYi1mZWYxLTQ1MDYtYTExMC1mMWNlN2JjNjQzOGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMteW91ci1yZWFsbSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhcGktZ2F0ZXdheS1jbGllbnQiOnsicm9sZXMiOlsiUk9MRV9BRE1JTiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsInNpZCI6IjZhODhjOWFiLWZlZjEtNDUwNi1hMTEwLWYxY2U3YmM2NDM4YyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiU3VuaWwgS3VtYXIgSyIsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3RhZG1pbiIsImdpdmVuX25hbWUiOiJTdW5pbCIsImZhbWlseV9uYW1lIjoiS3VtYXIgSyIsImVtYWlsIjoic3VuaWxrb3R0b2RpQGdtYWlsLmNvbSJ9.h-zGvfIFeoy8etNivO0DICGFIUye2WWMTbyyxrYMNSJJl70XV7cc3SBS5KazEHgp1CyNJR0Q6Ri9_cJ9d5gqTByW_NwsDnM7PAgna2vMPgyZIp8f0tuVkuBPyeNbx3025LK9awH-wsAPfR11yrd4_fm6vZS9TkOx_V8IXxMnyWgoTfPHerCeyp6ViFtQHYKr24jfqk9hSVr9CGo7QtTSTQwztcOmTVebh7NCZu_8QWvysZy0T_dkA1WV1-e93kq0mUpQS4i4bURuFxGdv2D3hmuqQqNzh0Wa4gMcNCWE6yTYGH9T3ycHQRfsdCEPLXThMseQZx2xnSgvFTGSaIU4KA")
//                .bodyValue("{}")
//                .exchange()
//                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE)
//                .expectBody()
//                .jsonPath("$").isEqualTo("Order Service is unavailable. Please try again later.");
//    }
}