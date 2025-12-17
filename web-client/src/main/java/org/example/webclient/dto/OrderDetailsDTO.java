package org.example.webclient.dto;

import java.util.List;

/**
 * Aggregated DTO for displaying order details with payment information.
 * This DTO combines data from OrderService and PaymentService.
 */
public class OrderDetailsDTO {
    private OrderDTO order;
    private List<PaymentDTO> payments;
    private boolean hasPaid;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(OrderDTO order, List<PaymentDTO> payments) {
        this.order = order;
        this.payments = payments;
        this.hasPaid = payments != null && !payments.isEmpty();
    }

    // Getters and Setters
    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
        this.hasPaid = payments != null && !payments.isEmpty();
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }
}

