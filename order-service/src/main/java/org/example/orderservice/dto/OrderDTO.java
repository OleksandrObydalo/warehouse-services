package org.example.orderservice.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private String orderId;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer rackCount;
    private String desiredType;
    private List<String> assignedRacks;
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String userId, LocalDate startDate, LocalDate endDate,
                   Integer rackCount, String desiredType, List<String> assignedRacks, 
                   String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rackCount = rackCount;
        this.desiredType = desiredType;
        this.assignedRacks = assignedRacks;
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

    public String getDesiredType() {
        return desiredType;
    }

    public void setDesiredType(String desiredType) {
        this.desiredType = desiredType;
    }

    public List<String> getAssignedRacks() {
        return assignedRacks;
    }

    public void setAssignedRacks(List<String> assignedRacks) {
        this.assignedRacks = assignedRacks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

