package org.example.webclient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private String orderId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @NotNull(message = "Rack count is required")
    @Min(value = 1, message = "Rack count must be at least 1")
    private Integer rackCount;
    
    @NotBlank(message = "Desired type is required")
    private String desiredType;
    
    private List<String> assignedRacks;
    private String status;

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

