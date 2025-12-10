package org.example.orderservice.dto;

import java.time.LocalDate;

public class DateRangeRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;

    public DateRangeRequestDTO() {
    }

    public DateRangeRequestDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
}

