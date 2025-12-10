package org.example.placeservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "places")
public class Place {
    @Id
    private String rackId;
    
    private String sectionCode;
    private Integer number;
    
    @Enumerated(EnumType.STRING)
    private RackType type;
    
    @Enumerated(EnumType.STRING)
    private RackStatus status;
    
    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;
    
    private Integer width;
    private Integer height;
    private Integer depth;
    
    @Column(name = "tenant_id")
    private String tenantId;

    public Place() {
    }

    public Place(String rackId, String sectionCode, Integer number, RackType type, 
                 RackStatus status, BigDecimal pricePerDay, Integer width, 
                 Integer height, Integer depth, String tenantId) {
        this.rackId = rackId;
        this.sectionCode = sectionCode;
        this.number = number;
        this.type = type;
        this.status = status;
        this.pricePerDay = pricePerDay;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.tenantId = tenantId;
    }

    // Getters and Setters
    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public RackType getType() {
        return type;
    }

    public void setType(RackType type) {
        this.type = type;
    }

    public RackStatus getStatus() {
        return status;
    }

    public void setStatus(RackStatus status) {
        this.status = status;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public enum RackType {
        STANDARD, REFRIGERATED, SECURE
    }

    public enum RackStatus {
        FREE, OCCUPIED
    }
}

