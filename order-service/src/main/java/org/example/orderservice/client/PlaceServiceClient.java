package org.example.orderservice.client;

import org.example.orderservice.dto.PlaceServiceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaceServiceClient {

    private final RestTemplate restTemplate;
    private final String placeServiceUrl;

    public PlaceServiceClient(RestTemplate restTemplate, 
                             @Value("${place.service.url}") String placeServiceUrl) {
        this.restTemplate = restTemplate;
        this.placeServiceUrl = placeServiceUrl;
    }

    public List<PlaceServiceDTO> getFreePlacesByType(String type) {
        try {
            String url = placeServiceUrl + "/api/places/free/type/" + type;
            ResponseEntity<List<PlaceServiceDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PlaceServiceDTO>>() {}
            );
            List<PlaceServiceDTO> places = response.getBody() != null ? response.getBody() : new ArrayList<>();
            
            // Validate response using JSON schema (fail-fast)
            if (response.getStatusCode().is2xxSuccessful() && !places.isEmpty()) {
                // Validation can be added here if needed
            }
            
            return places;
        } catch (HttpClientErrorException e) {
            // Fail-fast: throw exception immediately
            throw new RuntimeException("Place Service error: " + e.getMessage() + 
                    " (HTTP " + e.getStatusCode().value() + ")");
        } catch (ResourceAccessException e) {
            // Fail-fast: throw exception immediately
            throw new RuntimeException("Place Service unavailable: " + e.getMessage());
        }
    }

    public void givePlacesToUser(List<String> placeIds, String userId) {
        try {
            String url = placeServiceUrl + "/api/places/give";
            GivePlacesRequest request = new GivePlacesRequest(placeIds, userId);
            restTemplate.postForEntity(url, request, Void.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to give places: " + e.getMessage());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Place Service unavailable: " + e.getMessage());
        }
    }

    public void makePlacesFree(List<String> placeIds) {
        try {
            String url = placeServiceUrl + "/api/places/free";
            restTemplate.postForEntity(url, placeIds, Void.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to free places: " + e.getMessage());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Place Service unavailable: " + e.getMessage());
        }
    }

    public static class GivePlacesRequest {
        private List<String> placeIds;
        private String userId;

        public GivePlacesRequest() {
        }

        public GivePlacesRequest(List<String> placeIds, String userId) {
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
}

