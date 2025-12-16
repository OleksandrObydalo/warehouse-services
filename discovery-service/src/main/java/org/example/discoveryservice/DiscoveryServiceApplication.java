package org.example.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Discovery Service - Netflix Eureka Server
 * 
 * This service provides service discovery and registration for all microservices
 * in the warehouse management system. All microservices register themselves with
 * this Eureka server and can discover each other dynamically.
 * 
 * Port: 8761 (default Eureka port)
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}

