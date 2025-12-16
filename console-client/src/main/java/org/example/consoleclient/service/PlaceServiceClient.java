package org.example.consoleclient.service;

import org.example.consoleclient.dto.PlaceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PlaceServiceClient {

    private final RestTemplate restTemplate;
    private final String apiGatewayUrl;

    public PlaceServiceClient(RestTemplate restTemplate,
                             @Value("${api.gateway.url}") String apiGatewayUrl) {
        this.restTemplate = restTemplate;
        this.apiGatewayUrl = apiGatewayUrl;
    }

    public List<PlaceDTO> getAllFreePlaces() {
        String url = apiGatewayUrl + "/api/places/free";
        ResponseEntity<List<PlaceDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlaceDTO>>() {}
        );
        return response.getBody();
    }

    public List<PlaceDTO> getPlacesByUserId(String userId) {
        String url = apiGatewayUrl + "/api/places/user/" + userId;
        ResponseEntity<List<PlaceDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlaceDTO>>() {}
        );
        return response.getBody();
    }
}

