package org.example.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreatePaymentRequestDTO {
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private LocalDateTime date;

    public CreatePaymentRequestDTO() {
    }

    public CreatePaymentRequestDTO(String orderId, String userId, BigDecimal amount, LocalDateTime date) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

