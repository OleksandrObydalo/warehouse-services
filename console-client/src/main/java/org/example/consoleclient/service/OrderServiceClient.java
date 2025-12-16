package org.example.consoleclient.service;

import org.example.consoleclient.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceClient {

    private final RestTemplate restTemplate;
    private final String apiGatewayUrl;

    public OrderServiceClient(RestTemplate restTemplate,
                             @Value("${api.gateway.url}") String apiGatewayUrl) {
        this.restTemplate = restTemplate;
        this.apiGatewayUrl = apiGatewayUrl;
    }

    public OrderDTO createOrderForPlaces(Object request) {
        String url = apiGatewayUrl + "/api/orders";
        ResponseEntity<OrderDTO> response = restTemplate.postForEntity(
                url, request, OrderDTO.class);
        return response.getBody();
    }

    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        String url = apiGatewayUrl + "/api/orders/date-range?startDate=" + 
                startDate + "&endDate=" + endDate;
        ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {}
        );
        return response.getBody();
    }

    public OrderDTO confirmOrderById(String orderId) {
        String url = apiGatewayUrl + "/api/orders/" + orderId + "/confirm";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO cancelOrderById(String orderId) {
        String url = apiGatewayUrl + "/api/orders/" + orderId + "/cancel";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO startOrderById(String orderId) {
        String url = apiGatewayUrl + "/api/orders/" + orderId + "/start";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO finishOrderById(String orderId) {
        String url = apiGatewayUrl + "/api/orders/" + orderId + "/finish";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }
}

