package org.example.orderservice.dto;

import java.time.LocalDate;

public class CreateOrderRequestDTO {
    private Integer rackCount;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String desiredType;

    public CreateOrderRequestDTO() {
    }

    public CreateOrderRequestDTO(Integer rackCount, String userId, LocalDate startDate, 
                                 LocalDate endDate, String desiredType) {
        this.rackCount = rackCount;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.desiredType = desiredType;
    }

    // Getters and Setters
    public Integer getRackCount() {
        return rackCount;
    }

    public void setRackCount(Integer rackCount) {
        this.rackCount = rackCount;
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

    public String getDesiredType() {
        return desiredType;
    }

    public void setDesiredType(String desiredType) {
        this.desiredType = desiredType;
    }
}

