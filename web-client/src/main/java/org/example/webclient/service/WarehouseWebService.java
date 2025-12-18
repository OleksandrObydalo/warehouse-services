package org.example.webclient.service;

import org.example.webclient.dto.OrderDTO;
import org.example.webclient.dto.PaymentDTO;
import org.example.webclient.dto.PlaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class that handles all HTTP communication with the API Gateway.
 * This service acts as a client to the warehouse microservices through the gateway.
 */
@Service
public class WarehouseWebService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseWebService.class);

    private final RestTemplate restTemplate;
    private final String gatewayUrl;

    public WarehouseWebService(RestTemplate restTemplate, 
                                @Value("${gateway.url}") String gatewayUrl) {
        this.restTemplate = restTemplate;
        this.gatewayUrl = gatewayUrl;
        logger.info("WarehouseWebService initialized with gateway URL: {}", gatewayUrl);
    }

    // ==================== PLACE SERVICE ENDPOINTS ====================

    /**
     * Get list of free (available) places from PlaceService.
     * @return List of available places
     */
    public List<PlaceDTO> getFreePlaces() {
        String url = gatewayUrl + "/api/places/free";
        logger.debug("Fetching free places from: {}", url);
        
        ResponseEntity<List<PlaceDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlaceDTO>>() {}
        );
        
        logger.debug("Retrieved {} free places", response.getBody() != null ? response.getBody().size() : 0);
        return response.getBody();
    }

    // ==================== ORDER SERVICE ENDPOINTS ====================

    /**
     * Create a new order.
     * @param orderDTO Order data
     * @return Created order
     */
    public OrderDTO createOrder(OrderDTO orderDTO) {
        String url = gatewayUrl + "/api/orders";
        logger.debug("Creating order for user: {}", orderDTO.getUserId());
        
        HttpEntity<OrderDTO> request = new HttpEntity<>(orderDTO);
        ResponseEntity<OrderDTO> response = restTemplate.postForEntity(url, request, OrderDTO.class);
        
        logger.info("Order created with ID: {}", response.getBody() != null ? response.getBody().getOrderId() : "null");
        return response.getBody();
    }

    /**
     * Get orders within a date range.
     * @param startDate Start date
     * @param endDate End date
     * @return List of orders
     */
    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        String url = gatewayUrl + "/api/orders/date-range?startDate={startDate}&endDate={endDate}";
        logger.debug("Fetching orders from {} to {}", startDate, endDate);
        
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate.toString());
        params.put("endDate", endDate.toString());
        
        ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {},
                params
        );
        
        logger.debug("Retrieved {} orders", response.getBody() != null ? response.getBody().size() : 0);
        return response.getBody();
    }

    /**
     * Get all orders (without date filtering).
     * @return List of all orders
     */
    public List<OrderDTO> getAllOrders() {
        // Using a very wide date range to get all orders
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2100, 12, 31);
        return getOrdersByDateRange(startDate, endDate);
    }

    /**
     * Get a single order by ID.
     * @param orderId Order ID
     * @return Order details
     */
    public OrderDTO getOrderById(String orderId) {
        String url = gatewayUrl + "/api/orders/{id}";
        logger.debug("Fetching order with ID: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("id", orderId);
        
        ResponseEntity<OrderDTO> response = restTemplate.getForEntity(url, OrderDTO.class, params);
        
        logger.debug("Retrieved order: {}", orderId);
        return response.getBody();
    }

    /**
     * Confirm an order.
     * @param orderId Order ID to confirm
     * @return Confirmed order
     */
    public OrderDTO confirmOrder(String orderId) {
        String url = gatewayUrl + "/api/orders/{id}/confirm";
        logger.debug("Confirming order with ID: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("id", orderId);
        
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class,
                params
        );
        
        logger.info("Order confirmed: {}", orderId);
        return response.getBody();
    }

    /**
     * Cancel an order.
     * @param orderId Order ID to cancel
     * @return Cancelled order
     */
    public OrderDTO cancelOrder(String orderId) {
        String url = gatewayUrl + "/api/orders/{id}/cancel";
        logger.debug("Cancelling order with ID: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("id", orderId);
        
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class,
                params
        );
        
        logger.info("Order cancelled: {}", orderId);
        return response.getBody();
    }

    /**
     * Start an order.
     * @param orderId Order ID to start
     * @return Started order
     */
    public OrderDTO startOrder(String orderId) {
        String url = gatewayUrl + "/api/orders/{id}/start";
        logger.debug("Starting order with ID: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("id", orderId);
        
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class,
                params
        );
        
        logger.info("Order started: {}", orderId);
        return response.getBody();
    }

    /**
     * Finish an order.
     * @param orderId Order ID to finish
     * @return Finished order
     */
    public OrderDTO finishOrder(String orderId) {
        String url = gatewayUrl + "/api/orders/{id}/finish";
        logger.debug("Finishing order with ID: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("id", orderId);
        
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                null,
                OrderDTO.class,
                params
        );
        
        logger.info("Order finished: {}", orderId);
        return response.getBody();
    }

    // ==================== PAYMENT SERVICE ENDPOINTS ====================

    /**
     * Get payments for a specific order.
     * This is used for complex aggregation with order details.
     * @param orderId Order ID
     * @return List of payments for the order
     */
    public List<PaymentDTO> getPaymentsByOrderId(String orderId) {
        String url = gatewayUrl + "/api/payments/order/{orderId}";
        logger.debug("Fetching payments for order: {}", orderId);
        
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        
        try {
            ResponseEntity<List<PaymentDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PaymentDTO>>() {},
                    params
            );
            
            logger.debug("Retrieved {} payments for order {}", 
                    response.getBody() != null ? response.getBody().size() : 0, orderId);
            return response.getBody();
        } catch (Exception e) {
            logger.warn("Error fetching payments for order {}: {}", orderId, e.getMessage());
            // Return empty list if no payments found (instead of throwing exception)
            return List.of();
        }
    }

    /**
     * Create a payment for an order.
     * @param paymentDTO Payment data
     * @return Created payment
     */
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        String url = gatewayUrl + "/api/payments";
        logger.debug("Creating payment for order: {}", paymentDTO.getOrderId());
        
        HttpEntity<PaymentDTO> request = new HttpEntity<>(paymentDTO);
        ResponseEntity<PaymentDTO> response = restTemplate.postForEntity(url, request, PaymentDTO.class);
        
        logger.info("Payment created with ID: {}", response.getBody() != null ? response.getBody().getPaymentId() : "null");
        return response.getBody();
    }

    // ==================== PLACE SERVICE ENDPOINTS ====================

    /**
     * Get places rented by a specific user.
     * @param userId User ID
     * @return List of places rented by the user
     */
    public List<PlaceDTO> getPlacesByUserId(String userId) {
        String url = gatewayUrl + "/api/places/user/{userId}";
        logger.debug("Fetching places for user: {}", userId);
        
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        
        ResponseEntity<List<PlaceDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlaceDTO>>() {},
                params
        );
        
        logger.debug("Retrieved {} places for user {}", 
                response.getBody() != null ? response.getBody().size() : 0, userId);
        return response.getBody();
    }
}

