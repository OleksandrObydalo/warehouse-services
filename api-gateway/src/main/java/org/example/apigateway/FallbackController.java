package org.example.apigateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/place-service")
    public ResponseEntity<Map<String, String>> placeServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Place Service is currently unavailable");
        response.put("message", "Please try again later");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/order-service")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Order Service is currently unavailable");
        response.put("message", "Please try again later");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/payment-service")
    public ResponseEntity<Map<String, String>> paymentServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Payment Service is currently unavailable");
        response.put("message", "Please try again later");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}

