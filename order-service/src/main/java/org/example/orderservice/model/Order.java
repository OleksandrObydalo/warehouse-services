package org.example.orderservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "rack_count")
    private Integer rackCount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "desired_type")
    private RackType desiredType;
    
    @ElementCollection
    @CollectionTable(name = "order_assigned_racks", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "rack_id")
    private List<String> assignedRacks = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(String orderId, String userId, LocalDate startDate, LocalDate endDate,
                 Integer rackCount, RackType desiredType, OrderStatus status) {
        this.orderId = orderId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rackCount = rackCount;
        this.desiredType = desiredType;
        this.status = status;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getRackCount() {
        return rackCount;
    }

    public void setRackCount(Integer rackCount) {
        this.rackCount = rackCount;
    }

    public RackType getDesiredType() {
        return desiredType;
    }

    public void setDesiredType(RackType desiredType) {
        this.desiredType = desiredType;
    }

    public List<String> getAssignedRacks() {
        return assignedRacks;
    }

    public void setAssignedRacks(List<String> assignedRacks) {
        this.assignedRacks = assignedRacks;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public enum RackType {
        STANDARD, REFRIGERATED, SECURE
    }

    public enum OrderStatus {
        CREATED, CONFIRMED, ACTIVE, FINISHED, CANCELLED
    }
}

