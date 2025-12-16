package org.example.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Configuration with Client-Side Load Balancing
 * 
 * The @LoadBalanced annotation enables client-side load balancing
 * using Spring Cloud LoadBalancer. This allows RestTemplate to:
 * - Resolve service names (e.g., http://place-service) via Eureka
 * - Automatically distribute requests across multiple instances
 * - Support Round Robin load balancing by default
 */
@Configuration
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

