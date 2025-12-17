package org.example.webclient.dto;

import java.math.BigDecimal;

public class PlaceDTO {
    private String rackId;
    private String sectionCode;
    private Integer number;
    private String type;
    private String status;
    private BigDecimal pricePerDay;
    private DimensionsDTO dimensions;
    private String tenantId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public DimensionsDTO getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionsDTO dimensions) {
        this.dimensions = dimensions;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public static class DimensionsDTO {
        private Integer width;
        private Integer height;
        private Integer depth;

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
    }
}

