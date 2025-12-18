package org.example.webclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.webclient.dto.OrderDTO;
import org.example.webclient.dto.PaymentDTO;
import org.example.webclient.service.WarehouseWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller for viewing payments.
 */
@Controller
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final WarehouseWebService warehouseService;

    public PaymentController(WarehouseWebService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Display payments for a specific order.
     * Users can only view payments for their own orders.
     */
    @GetMapping("/order/{orderId}")
    public String showPaymentsByOrder(@PathVariable String orderId, HttpSession session, Model model) {
        String loggedInUserId = (String) session.getAttribute("userId");
        logger.info("Fetching payments for order: {} by user: {}", orderId, loggedInUserId);
        
        // ACCESS CONTROL: Check if order belongs to logged-in user
        OrderDTO order = warehouseService.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        if (!order.getUserId().equals(loggedInUserId)) {
            logger.warn("User {} attempted to view payments for order {} belonging to user {}", 
                    loggedInUserId, orderId, order.getUserId());
            throw new RuntimeException("Access denied: You can only view payments for your own orders");
        }
        
        List<PaymentDTO> payments = warehouseService.getPaymentsByOrderId(orderId);
        model.addAttribute("payments", payments);
        model.addAttribute("orderId", orderId);
        
        logger.info("Displaying {} payments for order {}", 
                payments != null ? payments.size() : 0, orderId);
        
        return "payments/list";
    }
}

