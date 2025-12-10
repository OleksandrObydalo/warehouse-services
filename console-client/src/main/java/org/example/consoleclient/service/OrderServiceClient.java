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
    private final String orderServiceUrl;

    public OrderServiceClient(RestTemplate restTemplate,
                             @Value("${order.service.url}") String orderServiceUrl) {
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }

    public OrderDTO createOrderForPlaces(Object request) {
        String url = orderServiceUrl + "/api/orders";
        ResponseEntity<OrderDTO> response = restTemplate.postForEntity(
                url, request, OrderDTO.class);
        return response.getBody();
    }

    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        String url = orderServiceUrl + "/api/orders/date-range?startDate=" + 
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
        String url = orderServiceUrl + "/api/orders/" + orderId + "/confirm";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO cancelOrderById(String orderId) {
        String url = orderServiceUrl + "/api/orders/" + orderId + "/cancel";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO startOrderById(String orderId) {
        String url = orderServiceUrl + "/api/orders/" + orderId + "/start";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }

    public OrderDTO finishOrderById(String orderId) {
        String url = orderServiceUrl + "/api/orders/" + orderId + "/finish";
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class
        );
        return response.getBody();
    }
}

