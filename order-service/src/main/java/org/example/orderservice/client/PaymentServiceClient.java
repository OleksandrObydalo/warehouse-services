package org.example.orderservice.client;

import org.example.orderservice.dto.PaymentServiceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentServiceClient(RestTemplate restTemplate,
                               @Value("${payment.service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public List<PaymentServiceDTO> getPaymentsByOrderId(String orderId) {
        try {
            String url = paymentServiceUrl + "/api/payments/order/" + orderId;
            ResponseEntity<List<PaymentServiceDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PaymentServiceDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            // Fail-silent: return empty list if payment service returns error
            // This prevents cascade failures when checking for payments
            System.err.println("Payment Service error (fail-silent): " + e.getMessage());
            return new ArrayList<>();
        } catch (ResourceAccessException e) {
            // Fail-silent: return empty list if payment service is unavailable
            // This prevents cascade failures when payment service is down
            System.err.println("Payment Service unavailable (fail-silent): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean hasPaymentForOrder(String orderId) {
        List<PaymentServiceDTO> payments = getPaymentsByOrderId(orderId);
        return payments != null && !payments.isEmpty();
    }
}

