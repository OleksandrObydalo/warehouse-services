package org.example.consoleclient.service;

import org.example.consoleclient.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentServiceClient(RestTemplate restTemplate,
                               @Value("${payment.service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public PaymentDTO createPayment(Object request) {
        String url = paymentServiceUrl + "/api/payments";
        ResponseEntity<PaymentDTO> response = restTemplate.postForEntity(
                url, request, PaymentDTO.class);
        return response.getBody();
    }

    public List<PaymentDTO> getAllPayments() {
        String url = paymentServiceUrl + "/api/payments";
        ResponseEntity<List<PaymentDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentDTO>>() {}
        );
        return response.getBody();
    }

    public List<PaymentDTO> getPaymentsByUserId(String userId) {
        String url = paymentServiceUrl + "/api/payments/user/" + userId;
        ResponseEntity<List<PaymentDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentDTO>>() {}
        );
        return response.getBody();
    }

    public List<PaymentDTO> getPaymentsByOrderId(String orderId) {
        String url = paymentServiceUrl + "/api/payments/order/" + orderId;
        ResponseEntity<List<PaymentDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentDTO>>() {}
        );
        return response.getBody();
    }
}

