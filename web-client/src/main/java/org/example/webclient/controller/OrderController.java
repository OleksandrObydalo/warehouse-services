package org.example.webclient.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.webclient.dto.OrderDTO;
import org.example.webclient.dto.OrderDetailsDTO;
import org.example.webclient.dto.PaymentDTO;
import org.example.webclient.service.WarehouseWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing orders.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final WarehouseWebService warehouseService;

    public OrderController(WarehouseWebService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Helper method to check if logged-in user owns the order.
     */
    private void checkOrderOwnership(String orderId, String loggedInUserId) {
        OrderDTO order = warehouseService.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        if (!order.getUserId().equals(loggedInUserId)) {
            logger.warn("User {} attempted to access order {} belonging to user {}", 
                    loggedInUserId, orderId, order.getUserId());
            throw new RuntimeException("Access denied: You can only access your own orders");
        }
    }

    /**
     * Display list of orders for the logged-in user.
     */
    @GetMapping
    public String listOrders(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        logger.info("Fetching orders for user: {}", userId);
        
        List<OrderDTO> allOrders = warehouseService.getAllOrders();
        // Filter orders to show only the logged-in user's orders
        List<OrderDTO> userOrders = allOrders.stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
        
        model.addAttribute("orders", userOrders);
        
        logger.info("Displaying {} orders for user {}", userOrders.size(), userId);
        return "orders/list";
    }

    /**
     * Show the form for creating a new order.
     */
    @GetMapping("/create")
    public String showCreateOrderForm(HttpSession session, Model model) {
        OrderDTO order = new OrderDTO();
        // Pre-fill user ID from session
        order.setUserId((String) session.getAttribute("userId"));
        model.addAttribute("order", order);
        return "orders/create";
    }

    /**
     * Handle order creation form submission.
     */
    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") OrderDTO orderDTO,
                            BindingResult bindingResult,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        
        String loggedInUserId = (String) session.getAttribute("userId");
        
        // Ensure user can only create orders for themselves
        orderDTO.setUserId(loggedInUserId);
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in order creation: {}", bindingResult.getAllErrors());
            return "orders/create";
        }

        // Validate that end date is after start date
        if (orderDTO.getEndDate() != null && orderDTO.getStartDate() != null 
                && orderDTO.getEndDate().isBefore(orderDTO.getStartDate())) {
            model.addAttribute("dateError", "End date must be after start date");
            return "orders/create";
        }

        try {
            logger.info("Creating order for user: {}", orderDTO.getUserId());
            OrderDTO createdOrder = warehouseService.createOrder(orderDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Order created successfully! Order ID: " + createdOrder.getOrderId());
            
            return "redirect:/orders/" + createdOrder.getOrderId();
        } catch (Exception e) {
            logger.error("Error creating order", e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }

    /**
     * Display order details with payment information (Complex Aggregation).
     * This method demonstrates the required complex aggregation:
     * 1. Fetch order details from OrderService
     * 2. Fetch payment information from PaymentService
     * 3. Combine and display on a single page
     */
    @GetMapping("/{orderId}")
    public String showOrderDetails(@PathVariable String orderId, HttpSession session, Model model) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Fetching details for order: {} by user: {}", orderId, loggedInUserId);
        
        // Step 1: Fetch order details from OrderService via Gateway
        OrderDTO order = warehouseService.getOrderById(orderId);
        
        if (order == null) {
            logger.warn("Order not found: {}", orderId);
            throw new RuntimeException("Order not found: " + orderId);
        }
        
        // ACCESS CONTROL: Check if order belongs to logged-in user
        if (!order.getUserId().equals(loggedInUserId)) {
            logger.warn("User {} attempted to access order {} belonging to user {}", 
                    loggedInUserId, orderId, order.getUserId());
            throw new RuntimeException("Access denied: You can only view your own orders");
        }
        
        // Step 2: Fetch payment information from PaymentService via Gateway
        // This is the COMPLEX AGGREGATION - combining data from two services
        List<PaymentDTO> payments = warehouseService.getPaymentsByOrderId(orderId);
        
        // Step 3: Create aggregated DTO with both order and payment data
        OrderDetailsDTO orderDetails = new OrderDetailsDTO(order, payments);
        
        model.addAttribute("orderDetails", orderDetails);
        
        logger.info("Displaying order {} with {} payments", orderId, 
                payments != null ? payments.size() : 0);
        
        return "orders/details";
    }

    /**
     * Confirm an order.
     */
    @PostMapping("/{orderId}/confirm")
    public String confirmOrder(@PathVariable String orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Confirming order: {} by user: {}", orderId, loggedInUserId);
        
        try {
            // Check ownership before confirming
            OrderDTO order = warehouseService.getOrderById(orderId);
            if (!order.getUserId().equals(loggedInUserId)) {
                throw new RuntimeException("Access denied: You can only confirm your own orders");
            }
            
            warehouseService.confirmOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Order confirmed successfully!");
            
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            logger.error("Error confirming order: {}", orderId, e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }

    /**
     * Cancel an order.
     */
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable String orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Cancelling order: {} by user: {}", orderId, loggedInUserId);
        
        try {
            checkOrderOwnership(orderId, loggedInUserId);
            warehouseService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Order cancelled successfully!");
            
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            logger.error("Error cancelling order: {}", orderId, e);
            throw e;
        }
    }

    /**
     * Start an order.
     */
    @PostMapping("/{orderId}/start")
    public String startOrder(@PathVariable String orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Starting order: {} by user: {}", orderId, loggedInUserId);
        
        try {
            checkOrderOwnership(orderId, loggedInUserId);
            warehouseService.startOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Order started successfully!");
            
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            logger.error("Error starting order: {}", orderId, e);
            throw e;
        }
    }

    /**
     * Finish an order.
     */
    @PostMapping("/{orderId}/finish")
    public String finishOrder(@PathVariable String orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Finishing order: {} by user: {}", orderId, loggedInUserId);
        
        try {
            checkOrderOwnership(orderId, loggedInUserId);
            warehouseService.finishOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Order finished successfully! Places have been freed.");
            
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            logger.error("Error finishing order: {}", orderId, e);
            throw e;
        }
    }

    /**
     * Show payment form for an order.
     */
    @GetMapping("/{orderId}/payment/add")
    public String showAddPaymentForm(@PathVariable String orderId, HttpSession session, Model model) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Showing payment form for order: {} by user: {}", orderId, loggedInUserId);
        
        checkOrderOwnership(orderId, loggedInUserId);
        
        OrderDTO order = warehouseService.getOrderById(orderId);
        
        PaymentDTO payment = new PaymentDTO();
        payment.setOrderId(orderId);
        payment.setUserId(order.getUserId());
        
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        
        return "orders/add-payment";
    }

    /**
     * Process payment creation.
     */
    @PostMapping("/{orderId}/payment/add")
    public String addPayment(@PathVariable String orderId,
                            @ModelAttribute PaymentDTO payment,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Adding payment for order: {} by user: {}", orderId, loggedInUserId);
        
        try {
            checkOrderOwnership(orderId, loggedInUserId);
            
            payment.setOrderId(orderId);
            payment.setUserId(loggedInUserId);
            PaymentDTO createdPayment = warehouseService.createPayment(payment);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Payment added successfully! Amount: $" + createdPayment.getAmount());
            
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            logger.error("Error adding payment for order: {}", orderId, e);
            throw e;
        }
    }
}

