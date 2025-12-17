package org.example.webclient.controller;

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
     */
    @GetMapping("/order/{orderId}")
    public String showPaymentsByOrder(@PathVariable String orderId, Model model) {
        logger.info("Fetching payments for order: {}", orderId);
        
        List<PaymentDTO> payments = warehouseService.getPaymentsByOrderId(orderId);
        model.addAttribute("payments", payments);
        model.addAttribute("orderId", orderId);
        
        logger.info("Displaying {} payments for order {}", 
                payments != null ? payments.size() : 0, orderId);
        
        return "payments/list";
    }
}

