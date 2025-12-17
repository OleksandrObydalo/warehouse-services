# Web Client - Quick Start Guide

## ⚡ Quick Start (5 Minutes)

### Step 1: Start Backend Services (2 minutes)

Open a terminal and run:

**Windows:**
```bash
start-all-services.bat
```

**Linux/Mac:**
```bash
./start-all-services.sh
```

Wait for all services to register with Eureka (check `http://localhost:8761`).

### Step 2: Start Web Client (1 minute)

Open a NEW terminal and run:

**Windows:**
```bash
start-web-client.bat
```

**Linux/Mac:**
```bash
chmod +x start-web-client.sh
./start-web-client.sh
```

### Step 3: Open Browser (30 seconds)

Navigate to:
```
http://localhost:8090
```

### Step 4: Test the Application (1 minute)

1. Click "Free Places" to see available warehouse spaces
2. Click "Create Order" to place a new order
3. Fill in the form and submit
4. Click "View Orders" to see all orders
5. Click "View" on an order to see the **Complex Aggregation** page (Order + Payment data)

## 🎯 What to Look For

### Complex Aggregation Feature
The Order Details page (`/orders/{orderId}`) is the key feature:
- Shows order information from **Order Service**
- Shows payment information from **Payment Service**
- Combines data from TWO microservices on ONE page
- This is the "Complex Aggregation" requirement ✅

### Error Handling
Try stopping a service and creating an order:
- You'll see a user-friendly error message
- NOT a stack trace ✅

### Form Validation
Try creating an order with invalid data:
- Empty fields → validation error
- End date before start date → validation error
- Both HTML5 and backend validation work ✅

## 🔗 Important URLs

| Service | URL | Port |
|---------|-----|------|
| **Web Client** | http://localhost:8090 | 8090 |
| API Gateway | http://localhost:8080 | 8080 |
| Discovery Service | http://localhost:8761 | 8761 |
| Place Service | http://localhost:8081 | 8081 |
| Order Service | http://localhost:8082 | 8082 |
| Payment Service | http://localhost:8083 | 8083 |

## 📱 Page Routes

| Page | Route | Description |
|------|-------|-------------|
| Home | `/` | Main page with navigation |
| Free Places | `/places/free` | Available warehouse places |
| Create Order | `/orders/create` | Order creation form |
| Orders List | `/orders` | All orders |
| **Order Details** | `/orders/{id}` | **Complex aggregation page** ⭐ |
| Payments | `/payments/order/{orderId}` | Payments for an order |

## 🐛 Troubleshooting

**Problem:** "Unable to connect to warehouse service"  
**Solution:** Start the backend services first

**Problem:** Port 8090 already in use  
**Solution:** Stop other apps on that port, or change port in `web-client/src/main/resources/application.properties`

**Problem:** No data displayed  
**Solution:** Backend services might be empty. Use console client or create orders via web UI

## 📖 Full Documentation

For complete documentation, see:
- `WEB_CLIENT_GUIDE.md` - Full implementation guide
- `web-client/README.md` - Web client specific docs
- `ARCHITECTURE.md` - System architecture

## ✅ Requirements Met

✅ Spring Boot 3.x with Spring MVC  
✅ Thymeleaf templates  
✅ Bootstrap 5 UI  
✅ RestTemplate for API calls  
✅ Port 8090  
✅ Service layer (WarehouseWebService)  
✅ Multiple views with navigation  
✅ Create order form with validation  
✅ **Complex aggregation (Order + Payment data)** ⭐  
✅ @ControllerAdvice for error handling  
✅ User-friendly error messages  

---

**Ready to go!** 🚀

If you encounter any issues, check the full guide in `WEB_CLIENT_GUIDE.md`.

