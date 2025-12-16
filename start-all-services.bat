@echo off
echo Starting all microservices with Service Discovery and API Gateway...
echo.

echo [1/5] Starting Discovery Service (Eureka Server)...
start "Discovery Service" cmd /k "cd discovery-service && mvn spring-boot:run"
echo Waiting for Discovery Service to start (port 8761)...
timeout /t 15 /nobreak >nul

echo [2/5] Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
echo Waiting for API Gateway to start (port 8080)...
timeout /t 10 /nobreak >nul

echo [3/5] Starting Place Service...
start "Place Service" cmd /k "cd place-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [4/5] Starting Order Service...
start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [5/5] Starting Payment Service...
start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo All services are starting...
echo ========================================
echo Discovery Service (Eureka): http://localhost:8761
echo API Gateway (Entry Point):  http://localhost:8080
echo Place Service:              http://localhost:8081
echo Order Service:              http://localhost:8082
echo Payment Service:            http://localhost:8083
echo ========================================
echo.
echo Access all services through API Gateway:
echo   Places:   http://localhost:8080/api/places
echo   Orders:   http://localhost:8080/api/orders
echo   Payments: http://localhost:8080/api/payments
echo.
echo Wait for all services to register with Eureka, then run the console client.
echo Check Eureka Dashboard: http://localhost:8761

