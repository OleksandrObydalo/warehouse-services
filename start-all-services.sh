#!/bin/bash

echo "Starting all microservices with Service Discovery and API Gateway..."
echo

echo "[1/5] Starting Discovery Service (Eureka Server)..."
cd discovery-service
mvn spring-boot:run &
DISCOVERY_PID=$!
cd ..
echo "Waiting for Discovery Service to start (port 8761)..."
sleep 15

echo "[2/5] Starting API Gateway..."
cd api-gateway
mvn spring-boot:run &
GATEWAY_PID=$!
cd ..
echo "Waiting for API Gateway to start (port 8080)..."
sleep 10

echo "[3/5] Starting Place Service..."
cd place-service
mvn spring-boot:run &
PLACE_PID=$!
cd ..
sleep 5

echo "[4/5] Starting Order Service..."
cd order-service
mvn spring-boot:run &
ORDER_PID=$!
cd ..
sleep 5

echo "[5/5] Starting Payment Service..."
cd payment-service
mvn spring-boot:run &
PAYMENT_PID=$!
cd ..
sleep 5

echo
echo "========================================"
echo "All services are starting..."
echo "========================================"
echo "Discovery Service (Eureka): http://localhost:8761 (PID: $DISCOVERY_PID)"
echo "API Gateway (Entry Point):  http://localhost:8080 (PID: $GATEWAY_PID)"
echo "Place Service:              http://localhost:8081 (PID: $PLACE_PID)"
echo "Order Service:              http://localhost:8082 (PID: $ORDER_PID)"
echo "Payment Service:            http://localhost:8083 (PID: $PAYMENT_PID)"
echo "========================================"
echo
echo "Access all services through API Gateway:"
echo "  Places:   http://localhost:8080/api/places"
echo "  Orders:   http://localhost:8080/api/orders"
echo "  Payments: http://localhost:8080/api/payments"
echo
echo "Wait for all services to register with Eureka, then run the console client."
echo "Check Eureka Dashboard: http://localhost:8761"
echo
echo "To stop all services, run: kill $DISCOVERY_PID $GATEWAY_PID $PLACE_PID $ORDER_PID $PAYMENT_PID"

