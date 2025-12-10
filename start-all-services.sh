#!/bin/bash

echo "Starting all microservices..."
echo

cd place-service
mvn spring-boot:run &
PLACE_PID=$!
cd ..

sleep 5

cd order-service
mvn spring-boot:run &
ORDER_PID=$!
cd ..

sleep 5

cd payment-service
mvn spring-boot:run &
PAYMENT_PID=$!
cd ..

echo "All services are starting..."
echo "Place Service: http://localhost:8081 (PID: $PLACE_PID)"
echo "Order Service: http://localhost:8082 (PID: $ORDER_PID)"
echo "Payment Service: http://localhost:8083 (PID: $PAYMENT_PID)"
echo
echo "To stop services, run: kill $PLACE_PID $ORDER_PID $PAYMENT_PID"

