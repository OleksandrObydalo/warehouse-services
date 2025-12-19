package org.example.orderservice.controller;

import org.example.orderservice.dto.CreateOrderRequestDTO;
import org.example.orderservice.dto.OrderDTO;
import org.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrderForPlaces(@RequestBody CreateOrderRequestDTO request) {
        OrderDTO order = orderService.createOrderForPlaces(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<OrderDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<OrderDTO> confirmOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.confirmOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.cancelOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/start")
    public ResponseEntity<OrderDTO> startOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.startOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/finish")
    public ResponseEntity<OrderDTO> finishOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.finishOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}

