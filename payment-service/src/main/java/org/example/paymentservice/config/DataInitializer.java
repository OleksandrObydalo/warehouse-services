package org.example.paymentservice.config;

import org.example.paymentservice.model.Payment;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void run(String... args) {
        // Initialize with sample data if needed
        // Payments will be created through API calls
    }
}

