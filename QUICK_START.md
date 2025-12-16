# Quick Start Guide - Spring Cloud Warehouse System

## Prerequisites
- Java 17+
- Maven 3.6+

## Step-by-Step Startup

### 1. Start Discovery Service (Eureka)
```bash
cd discovery-service
mvn spring-boot:run
```
**Wait 15 seconds** until you see: `Started DiscoveryServiceApplication`

### 2. Verify Eureka is Running
Open browser: http://localhost:8761
You should see the Eureka Dashboard.

### 3. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
**Wait 10 seconds** until you see: `Started ApiGatewayApplication`

### 4. Start All Microservices (in parallel)

**Terminal 1:**
```bash
cd place-service
mvn spring-boot:run
```

**Terminal 2:**
```bash
cd order-service
mvn spring-boot:run
```

**Terminal 3:**
```bash
cd payment-service
mvn spring-boot:run
```

**Wait 5-10 seconds** for all services to start.

### 5. Verify All Services are Registered

Open Eureka Dashboard: http://localhost:8761

You should see:
- ✅ API-GATEWAY
- ✅ PLACE-SERVICE
- ✅ ORDER-SERVICE
- ✅ PAYMENT-SERVICE

### 6. Test the System

**Test via API Gateway:**
```bash
# Get all free places
curl http://localhost:8080/api/places/free

# Get orders
curl http://localhost:8080/api/orders/date-range?startDate=2024-01-01&endDate=2024-12-31

# Get payments
curl http://localhost:8080/api/payments
```

### 7. Start Console Client
```bash
cd console-client
mvn spring-boot:run
```

Follow the interactive menu to test the system.

## Automated Startup

### Windows
```bash
start-all-services.bat
```

### Linux/Mac
```bash
chmod +x start-all-services.sh
./start-all-services.sh
```

## Troubleshooting

### Services Not Appearing in Eureka
- **Solution**: Wait 30 seconds. Eureka has a default heartbeat interval.
- **Check**: Service logs for connection errors to port 8761.

### API Gateway Returns 503 Service Unavailable
- **Solution**: Ensure all services are registered in Eureka.
- **Check**: Eureka Dashboard at http://localhost:8761

### Console Client Cannot Connect
- **Solution**: Verify API Gateway is running on port 8080.
- **Test**: `curl http://localhost:8080/api/places/free`

## Useful URLs

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Service Registry |
| API Gateway | http://localhost:8080 | Single Entry Point |
| Place Service | http://localhost:8081 | Direct Access (Dev) |
| Order Service | http://localhost:8082 | Direct Access (Dev) |
| Payment Service | http://localhost:8083 | Direct Access (Dev) |
| Place Swagger | http://localhost:8081/swagger-ui.html | API Docs |
| Order Swagger | http://localhost:8082/swagger-ui.html | API Docs |
| Payment Swagger | http://localhost:8083/swagger-ui.html | API Docs |

## Load Balancing Demo

### Start Multiple Instances of Place Service

**Terminal 1:**
```bash
cd place-service
mvn spring-boot:run
```

**Terminal 2:**
```bash
cd place-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8091
```

**Terminal 3:**
```bash
cd place-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8092
```

Check Eureka Dashboard - you'll see 3 instances of PLACE-SERVICE.

Create orders through the console client - requests will be distributed across all three instances (Round Robin).

## Stopping Services

### Windows
Close each terminal window or press `Ctrl+C` in each terminal.

### Linux/Mac
```bash
# If you saved PIDs from start script
kill $DISCOVERY_PID $GATEWAY_PID $PLACE_PID $ORDER_PID $PAYMENT_PID

# Or find and kill all Java processes
pkill -f "spring-boot:run"
```

## Next Steps

📖 Read the full documentation: [LAB4_SPRING_CLOUD_GUIDE.md](LAB4_SPRING_CLOUD_GUIDE.md)

🔧 Explore the code:
- Discovery Service: `discovery-service/`
- API Gateway: `api-gateway/`
- Microservices: `place-service/`, `order-service/`, `payment-service/`

🧪 Test the APIs: [API_TEST_REQUESTS.md](API_TEST_REQUESTS.md)

