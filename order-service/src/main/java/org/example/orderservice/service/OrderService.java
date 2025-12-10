package org.example.orderservice.service;

import org.example.orderservice.client.PaymentServiceClient;
import org.example.orderservice.client.PlaceServiceClient;
import org.example.orderservice.dto.*;
import org.example.orderservice.model.Order;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PlaceServiceClient placeServiceClient;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Transactional
    public OrderDTO createOrderForPlaces(CreateOrderRequestDTO request) {
        // Check if enough free places are available
        List<PlaceServiceDTO> freePlaces = placeServiceClient.getFreePlacesByType(request.getDesiredType());
        
        if (freePlaces.size() < request.getRackCount()) {
            throw new RuntimeException("Not enough free places available. Required: " + 
                    request.getRackCount() + ", Available: " + freePlaces.size());
        }

        // Create order
        String orderId = "ord" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(
                orderId,
                request.getUserId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getRackCount(),
                Order.RackType.valueOf(request.getDesiredType()),
                Order.OrderStatus.CREATED
        );

        orderRepository.save(order);
        return convertToDTO(order);
    }

    public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
                endDate, startDate);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO confirmOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != Order.OrderStatus.CREATED) {
            throw new RuntimeException("Order cannot be confirmed. Current status: " + order.getStatus());
        }

        // Check if payment exists (fail-silent if payment service is unavailable)
        boolean hasPayment = paymentServiceClient.hasPaymentForOrder(orderId);
        if (!hasPayment) {
            throw new RuntimeException("Order cannot be confirmed without payment");
        }

        // Assign places
        List<PlaceServiceDTO> freePlaces = placeServiceClient.getFreePlacesByType(
                order.getDesiredType().name());
        
        if (freePlaces.size() < order.getRackCount()) {
            throw new RuntimeException("Not enough free places available");
        }

        List<String> placeIds = freePlaces.stream()
                .limit(order.getRackCount())
                .map(PlaceServiceDTO::getRackId)
                .collect(Collectors.toList());

        placeServiceClient.givePlacesToUser(placeIds, order.getUserId());
        order.setAssignedRacks(placeIds);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO cancelOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() == Order.OrderStatus.FINISHED || 
            order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Order cannot be cancelled. Current status: " + order.getStatus());
        }

        // Free places if they were assigned
        if (order.getAssignedRacks() != null && !order.getAssignedRacks().isEmpty()) {
            try {
                placeServiceClient.makePlacesFree(order.getAssignedRacks());
            } catch (Exception e) {
                // Log error but continue with cancellation
                System.err.println("Error freeing places: " + e.getMessage());
            }
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);

        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO startOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order cannot be started. Current status: " + order.getStatus());
        }

        order.setStatus(Order.OrderStatus.ACTIVE);
        orderRepository.save(order);

        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO finishOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != Order.OrderStatus.ACTIVE) {
            throw new RuntimeException("Order cannot be finished. Current status: " + order.getStatus());
        }

        // Free places
        if (order.getAssignedRacks() != null && !order.getAssignedRacks().isEmpty()) {
            try {
                placeServiceClient.makePlacesFree(order.getAssignedRacks());
            } catch (Exception e) {
                // Log error but continue with finishing
                System.err.println("Error freeing places: " + e.getMessage());
            }
        }

        order.setStatus(Order.OrderStatus.FINISHED);
        orderRepository.save(order);

        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getUserId(),
                order.getStartDate(),
                order.getEndDate(),
                order.getRackCount(),
                order.getDesiredType().name(),
                order.getAssignedRacks(),
                order.getStatus().name()
        );
    }
}

