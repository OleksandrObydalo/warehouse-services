package org.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway - Single Entry Point for Warehouse Management System
 * 
 * This gateway provides:
 * - Single entry point for all microservices (port 8080)
 * - Dynamic routing via Service Discovery (Eureka)
 * - Client-side load balancing
 * - Centralized cross-cutting concerns (logging, security, etc.)
 * 
 * Routes:
 * - /api/places/** -> place-service
 * - /api/orders/** -> order-service
 * - /api/payments/** -> payment-service
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

