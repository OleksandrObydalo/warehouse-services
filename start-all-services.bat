@echo off
echo Starting all microservices...
echo.

start "Place Service" cmd /k "cd place-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo All services are starting...
echo Place Service: http://localhost:8081
echo Order Service: http://localhost:8082
echo Payment Service: http://localhost:8083
echo.
echo Wait for all services to start, then run the console client.

