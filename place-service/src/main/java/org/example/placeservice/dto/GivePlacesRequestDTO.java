package org.example.placeservice.dto;

import java.util.List;

public class GivePlacesRequestDTO {
    private List<String> placeIds;
    private String userId;

    public GivePlacesRequestDTO() {
    }

    public GivePlacesRequestDTO(List<String> placeIds, String userId) {
        this.placeIds = placeIds;
        this.userId = userId;
    }

    public List<String> getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(List<String> placeIds) {
        this.placeIds = placeIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

