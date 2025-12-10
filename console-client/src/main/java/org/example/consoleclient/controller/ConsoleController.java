package org.example.consoleclient.controller;

import org.example.consoleclient.dto.*;
import org.example.consoleclient.service.OrderServiceClient;
import org.example.consoleclient.service.PaymentServiceClient;
import org.example.consoleclient.service.PlaceServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleController implements CommandLineRunner {

    @Autowired
    private PlaceServiceClient placeServiceClient;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Warehouse Management System Console Client ===");
        System.out.println("Available commands:");
        printMenu();

        while (running) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim();

            try {
                switch (command.toLowerCase()) {
                    case "1":
                        handleGetAllFreePlaces();
                        break;
                    case "2":
                        handleGetPlacesByUserId(scanner);
                        break;
                    case "3":
                        handleCreateOrder(scanner);
                        break;
                    case "4":
                        handleGetOrdersByDateRange(scanner);
                        break;
                    case "5":
                        handleConfirmOrder(scanner);
                        break;
                    case "6":
                        handleCancelOrder(scanner);
                        break;
                    case "7":
                        handleStartOrder(scanner);
                        break;
                    case "8":
                        handleFinishOrder(scanner);
                        break;
                    case "9":
                        handleCreatePayment(scanner);
                        break;
                    case "10":
                        handleGetAllPayments();
                        break;
                    case "11":
                        handleGetPaymentsByUserId(scanner);
                        break;
                    case "12":
                        handleGetPaymentsByOrderId(scanner);
                        break;
                    case "help":
                        printMenu();
                        break;
                    case "exit":
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for menu or 'exit' to quit.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("1.  Get all free places");
        System.out.println("2.  Get places by user ID");
        System.out.println("3.  Create order");
        System.out.println("4.  Get orders by date range");
        System.out.println("5.  Confirm order");
        System.out.println("6.  Cancel order");
        System.out.println("7.  Start order");
        System.out.println("8.  Finish order");
        System.out.println("9.  Create payment");
        System.out.println("10. Get all payments");
        System.out.println("11. Get payments by user ID");
        System.out.println("12. Get payments by order ID");
        System.out.println("help - Show this menu");
        System.out.println("exit - Exit application");
    }

    private void handleGetAllFreePlaces() {
        System.out.println("\n=== All Free Places ===");
        List<PlaceDTO> places = placeServiceClient.getAllFreePlaces();
        places.forEach(p -> System.out.println("Rack ID: " + p.getRackId() + 
                ", Type: " + p.getType() + ", Price: " + p.getPricePerDay()));
    }

    private void handleGetPlacesByUserId(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.println("\n=== Places for User " + userId + " ===");
        List<PlaceDTO> places = placeServiceClient.getPlacesByUserId(userId);
        places.forEach(p -> System.out.println("Rack ID: " + p.getRackId() + 
                ", Status: " + p.getStatus()));
    }

    private void handleCreateOrder(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter rack count: ");
        int rackCount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter desired type (STANDARD/REFRIGERATED/SECURE): ");
        String type = scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        request.put("rackCount", rackCount);
        request.put("desiredType", type);
        request.put("startDate", startDate);
        request.put("endDate", endDate);

        OrderDTO order = orderServiceClient.createOrderForPlaces(request);
        System.out.println("Order created: " + order.getOrderId());
    }

    private void handleGetOrdersByDateRange(Scanner scanner) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        List<OrderDTO> orders = orderServiceClient.getOrdersByDateRange(startDate, endDate);
        System.out.println("\n=== Orders in Date Range ===");
        orders.forEach(o -> System.out.println("Order ID: " + o.getOrderId() + 
                ", Status: " + o.getStatus() + ", User: " + o.getUserId()));
    }

    private void handleConfirmOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        OrderDTO order = orderServiceClient.confirmOrderById(orderId);
        System.out.println("Order confirmed: " + order.getOrderId());
    }

    private void handleCancelOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        OrderDTO order = orderServiceClient.cancelOrderById(orderId);
        System.out.println("Order cancelled: " + order.getOrderId());
    }

    private void handleStartOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        OrderDTO order = orderServiceClient.startOrderById(orderId);
        System.out.println("Order started: " + order.getOrderId());
    }

    private void handleFinishOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        OrderDTO order = orderServiceClient.finishOrderById(orderId);
        System.out.println("Order finished: " + order.getOrderId());
    }

    private void handleCreatePayment(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        LocalDateTime date = LocalDateTime.now();

        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        request.put("userId", userId);
        request.put("amount", amount);
        request.put("date", date);

        PaymentDTO payment = paymentServiceClient.createPayment(request);
        System.out.println("Payment created: " + payment.getPaymentId());
    }

    private void handleGetAllPayments() {
        System.out.println("\n=== All Payments ===");
        List<PaymentDTO> payments = paymentServiceClient.getAllPayments();
        payments.forEach(p -> System.out.println("Payment ID: " + p.getPaymentId() + 
                ", Order: " + p.getOrderId() + ", Amount: " + p.getAmount()));
    }

    private void handleGetPaymentsByUserId(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.println("\n=== Payments for User " + userId + " ===");
        List<PaymentDTO> payments = paymentServiceClient.getPaymentsByUserId(userId);
        payments.forEach(p -> System.out.println("Payment ID: " + p.getPaymentId() + 
                ", Amount: " + p.getAmount()));
    }

    private void handleGetPaymentsByOrderId(Scanner scanner) {
        System.out.print("Enter order ID: ");
        String orderId = scanner.nextLine();
        System.out.println("\n=== Payments for Order " + orderId + " ===");
        List<PaymentDTO> payments = paymentServiceClient.getPaymentsByOrderId(orderId);
        payments.forEach(p -> System.out.println("Payment ID: " + p.getPaymentId() + 
                ", Amount: " + p.getAmount()));
    }
}

