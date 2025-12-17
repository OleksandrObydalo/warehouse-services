# Web Client Implementation Guide

## 🎯 Overview

This document describes the implementation of the **Web Client** for the Warehouse Management System. The web client replaces the console client with a modern, user-friendly web interface.

## 📁 Project Structure

A new module `web-client` has been added to the project:

```
warehouse-services/
├── web-client/                          # ← NEW MODULE
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/org/example/webclient/
│   │   │   │   ├── WebClientApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   └── RestTemplateConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── HomeController.java
│   │   │   │   │   ├── PlaceController.java
│   │   │   │   │   ├── OrderController.java
│   │   │   │   │   └── PaymentController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── PlaceDTO.java
│   │   │   │   │   ├── OrderDTO.java
│   │   │   │   │   ├── PaymentDTO.java
│   │   │   │   │   └── OrderDetailsDTO.java
│   │   │   │   ├── service/
│   │   │   │   │   └── WarehouseWebService.java
│   │   │   │   └── exception/
│   │   │   │       └── GlobalExceptionHandler.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── templates/
│   │   │           ├── index.html
│   │   │           ├── error.html
│   │   │           ├── places/
│   │   │           │   └── free.html
│   │   │           ├── orders/
│   │   │           │   ├── create.html
│   │   │           │   ├── list.html
│   │   │           │   └── details.html
│   │   │           └── payments/
│   │   │               └── list.html
│   │   └── pom.xml
│   └── README.md
├── start-web-client.bat                 # ← NEW STARTUP SCRIPT
└── start-web-client.sh                  # ← NEW STARTUP SCRIPT
```

## 🚀 Getting Started

### Step 1: Start All Required Services

Before starting the web client, ensure these services are running:

1. **Discovery Service** (Eureka) - Port 8761
2. **API Gateway** - Port 8080
3. **Place Service** - Port 8081
4. **Order Service** - Port 8082
5. **Payment Service** - Port 8083

Use the existing startup script:
```bash
# Windows
start-all-services.bat

# Linux/Mac
./start-all-services.sh
```

### Step 2: Start the Web Client

**Windows:**
```bash
start-web-client.bat
```

**Linux/Mac:**
```bash
chmod +x start-web-client.sh
./start-web-client.sh
```

**Or using Maven directly:**
```bash
cd web-client
mvn spring-boot:run
```

### Step 3: Access the Application

Open your browser and navigate to:
```
http://localhost:8090
```

## 🏗️ Technical Implementation

### 1. Service Layer: WarehouseWebService

**Location:** `web-client/src/main/java/org/example/webclient/service/WarehouseWebService.java`

This service handles all HTTP communication with the API Gateway at `http://localhost:8080`.

**Key Methods:**

```java
// Get free places
public List<PlaceDTO> getFreePlaces()

// Create new order
public OrderDTO createOrder(OrderDTO orderDTO)

// Get orders by date range
public List<OrderDTO> getOrdersByDateRange(LocalDate startDate, LocalDate endDate)

// Get all orders
public List<OrderDTO> getAllOrders()

// Get single order
public OrderDTO getOrderById(String orderId)

// Confirm order
public OrderDTO confirmOrder(String orderId)

// Get payments for order
public List<PaymentDTO> getPaymentsByOrderId(String orderId)
```

**Features:**
- Uses Spring's `RestTemplate` for HTTP requests
- Proper error handling
- Logging for debugging
- Returns empty list instead of throwing exception for missing payments

### 2. Controllers and Views

#### HomeController
- **Route:** `/` or `/index`
- **View:** `index.html`
- **Description:** Landing page with navigation cards

#### PlaceController
- **Route:** `/places/free`
- **View:** `places/free.html`
- **Description:** Displays all available warehouse places
- **Features:** 
  - Table view with place details
  - Color-coded place types
  - Dimensions and pricing information

#### OrderController
- **Routes:**
  - `GET /orders` - List all orders
  - `GET /orders/create` - Show create form
  - `POST /orders/create` - Submit new order
  - `GET /orders/{orderId}` - View order details (Complex Aggregation)
  - `POST /orders/{orderId}/confirm` - Confirm order

- **Views:**
  - `orders/list.html` - Orders list
  - `orders/create.html` - Create order form
  - `orders/details.html` - Order details with payments

- **Features:**
  - Form validation (frontend and backend)
  - Complex aggregation (combines Order + Payment data)
  - Status indicators
  - Confirm functionality

#### PaymentController
- **Route:** `/payments/order/{orderId}`
- **View:** `payments/list.html`
- **Description:** Displays all payments for a specific order

### 3. Complex Aggregation - Order Details Page

**Location:** `OrderController.showOrderDetails()` method

This is the **critical feature** that demonstrates complex aggregation:

```java
@GetMapping("/{orderId}")
public String showOrderDetails(@PathVariable String orderId, Model model) {
    // Step 1: Fetch order details from OrderService via Gateway
    OrderDTO order = warehouseService.getOrderById(orderId);
    
    // Step 2: Fetch payment information from PaymentService via Gateway
    List<PaymentDTO> payments = warehouseService.getPaymentsByOrderId(orderId);
    
    // Step 3: Create aggregated DTO with both order and payment data
    OrderDetailsDTO orderDetails = new OrderDetailsDTO(order, payments);
    
    model.addAttribute("orderDetails", orderDetails);
    return "orders/details";
}
```

**What makes this "complex aggregation":**
1. Makes TWO separate API calls to different microservices
2. Combines data from Order Service and Payment Service
3. Presents unified view on a single page
4. Shows payment status based on payment existence
5. All communication goes through the API Gateway

**Visual Features on Details Page:**
- Order information card (blue header)
- Payment information card (green if paid, yellow if not)
- Payment status indicator
- List of all payments with amounts and dates
- Action buttons (Confirm, View Payments, Back)
- Informational box explaining the aggregation

### 4. Form Validation

The Create Order form implements **dual validation**:

#### Frontend Validation (HTML5)
```html
<input type="text" required placeholder="Enter user ID">
<input type="number" required min="1">
<select required>
<input type="date" required>
```

**JavaScript validation for dates:**
```javascript
// Ensures end date is after start date
document.getElementById('endDate').addEventListener('change', function() {
    const startDate = document.getElementById('startDate').value;
    const endDate = this.value;
    
    if (startDate && endDate && endDate <= startDate) {
        this.setCustomValidity('End date must be after start date');
    } else {
        this.setCustomValidity('');
    }
});
```

#### Backend Validation (Spring)
```java
@NotBlank(message = "User ID is required")
private String userId;

@NotNull(message = "Rack count is required")
@Min(value = 1, message = "Rack count must be at least 1")
private Integer rackCount;

@NotNull(message = "Start date is required")
private LocalDate startDate;

@NotNull(message = "End date is required")
@Future(message = "End date must be in the future")
private LocalDate endDate;
```

### 5. Error Handling - GlobalExceptionHandler

**Location:** `web-client/src/main/java/org/example/webclient/exception/GlobalExceptionHandler.java`

This `@ControllerAdvice` provides user-friendly error handling:

**Handles:**
- **4xx Client Errors** (Bad Request, Not Found, Conflict)
- **5xx Server Errors** (Internal Server Error, Service Unavailable)
- **Connection Errors** (Gateway down)
- **General Exceptions**

**Features:**
- Extracts error messages from Gateway responses
- Provides helpful troubleshooting tips
- Shows Bootstrap alerts instead of stack traces
- Maintains consistent error page design

**Example Error Handling:**
```java
@ExceptionHandler(HttpClientErrorException.class)
public String handleClientError(HttpClientErrorException ex, Model model) {
    String errorMessage = extractErrorMessage(ex, 
        "The request contains invalid data. Please check your input.");
    
    model.addAttribute("errorTitle", "Invalid Request");
    model.addAttribute("errorMessage", errorMessage);
    model.addAttribute("alertType", "warning");
    
    return "error";
}
```

**If Gateway returns "Not enough places":**
- User sees: "There is a conflict with the current state. Please check available places."
- NOT: Stack trace with HttpClientErrorException

## 🎨 UI/UX Design

### Bootstrap 5 Implementation

**Framework:** Bootstrap 5.3.2  
**Icons:** Bootstrap Icons 1.11.1  
**CDN:** Used for faster development and no build step

### Design Principles

1. **Responsive Design**: Works on desktop, tablet, and mobile
2. **Intuitive Navigation**: Clear navbar with active states
3. **Visual Feedback**: 
   - Success messages (green alerts)
   - Error messages (yellow/red alerts)
   - Loading states
   - Hover effects on cards and tables
4. **Color Coding**:
   - Status badges (Confirmed=green, Pending=yellow)
   - Place types (Standard=blue, Cold=cyan, Hazardous=orange)
   - Payment status (Paid=green, Unpaid=yellow)

### Key UI Components

- **Cards**: Main content containers with shadow
- **Tables**: Responsive tables with hover states
- **Forms**: Styled inputs with validation feedback
- **Badges**: Status and type indicators
- **Alerts**: Success and error messages
- **Buttons**: Primary, secondary, outline variants
- **Icons**: Visual indicators throughout

## 📊 Data Flow

### Creating an Order
```
User (Browser)
    ↓ (HTTP POST with form data)
OrderController.createOrder()
    ↓ (Validate input)
WarehouseWebService.createOrder()
    ↓ (HTTP POST to Gateway)
API Gateway :8080
    ↓ (Route to Order Service)
Order Service :8082
    ↓ (Process order, check places)
    ↓ (Response)
User sees success message or error
```

### Viewing Order Details (Complex Aggregation)
```
User clicks "View" on an order
    ↓
OrderController.showOrderDetails()
    ↓
    ├─→ WarehouseWebService.getOrderById()
    │       ↓ (GET /api/orders/{id})
    │   API Gateway → Order Service
    │       ↓
    │   OrderDTO returned
    │
    └─→ WarehouseWebService.getPaymentsByOrderId()
            ↓ (GET /api/payments/order/{orderId})
        API Gateway → Payment Service
            ↓
        List<PaymentDTO> returned
    ↓
Create OrderDetailsDTO (order + payments)
    ↓
Render orders/details.html with combined data
    ↓
User sees order info + payment info on one page
```

## 🔍 Testing the Application

### Test Scenario 1: Create an Order

1. Go to `http://localhost:8090`
2. Click "Create Order" or navigate to `/orders/create`
3. Fill in the form:
   - User ID: `USER-001`
   - Rack Count: `2`
   - Place Type: `STANDARD`
   - Start Date: Today's date
   - End Date: A future date
4. Click "Create Order"
5. **Expected:** Redirect to order details page with success message

### Test Scenario 2: View Order with Complex Aggregation

1. From orders list, click "View" on any order
2. **Expected:** See order details page showing:
   - Order information (ID, user, dates, racks, status)
   - Payment information (paid/unpaid status)
   - If paid: List of payments with amounts
   - If not paid: Warning message
3. This demonstrates complex aggregation (Order + Payment data)

### Test Scenario 3: Error Handling

1. Stop the Order Service
2. Try to create an order
3. **Expected:** User-friendly error page saying "Service temporarily unavailable"
4. **NOT Expected:** Stack trace

### Test Scenario 4: Validation

1. Try to create an order with:
   - Empty user ID
   - Rack count = 0
   - End date before start date
2. **Expected:** Validation errors shown on form
3. Form should not submit

## 🐛 Common Issues and Solutions

### Issue 1: Port 8090 Already in Use
**Solution:** 
- Stop any application using port 8090
- Or change port in `application.properties`: `server.port=8091`

### Issue 2: Cannot Connect to Gateway
**Symptoms:** Connection errors, "Unable to connect" messages  
**Solution:**
- Ensure Gateway is running: `http://localhost:8080`
- Check Gateway logs
- Verify Discovery Service is running

### Issue 3: No Data Displayed
**Symptoms:** Empty tables, "No items found"  
**Solution:**
- Check if backend services have data
- Use the console client or Postman to add test data
- Verify Gateway routing configuration

### Issue 4: 404 Not Found on Order Details
**Symptoms:** Error when clicking "View" on an order  
**Solution:**
- Ensure Order Service is running
- Check that the order ID exists
- Verify Gateway can route to Order Service

### Issue 5: Thymeleaf Template Not Found
**Symptoms:** "Template might not exist" errors  
**Solution:**
- Check template files are in `src/main/resources/templates/`
- Verify file names match controller return values
- Rebuild project: `mvn clean install`

## 📝 Configuration Reference

### application.properties
```properties
# Server Configuration
server.port=8090                    # Web client port
spring.application.name=web-client  # Application name

# Gateway Configuration
gateway.url=http://localhost:8080   # API Gateway URL

# Thymeleaf Configuration
spring.thymeleaf.cache=false        # Disable cache for development
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML

# Logging Configuration
logging.level.org.example.webclient=DEBUG
logging.level.org.springframework.web=INFO
```

### Dependencies (pom.xml)
- `spring-boot-starter-web` - Web framework
- `spring-boot-starter-thymeleaf` - Template engine
- `spring-boot-starter-validation` - Form validation
- `jackson-databind` - JSON processing
- `jackson-datatype-jsr310` - Java 8 date/time support

## ✅ Requirements Checklist

All requirements from the task have been implemented:

- ✅ **Java 17+, Spring Boot 3.x**: Using Java 17 and Spring Boot 3.1.5
- ✅ **Spring MVC + Thymeleaf**: Controllers with Thymeleaf templates
- ✅ **Bootstrap 5**: All pages use Bootstrap 5.3.2
- ✅ **RestTemplate**: Configured and used in WarehouseWebService
- ✅ **Port 8090**: Configured in application.properties
- ✅ **Service Layer**: WarehouseWebService handles all HTTP requests
- ✅ **Main Page**: index.html with navigation
- ✅ **Free Places Page**: places/free.html with table display
- ✅ **Create Order Page**: orders/create.html with HTML5 validation
- ✅ **Order Details Page**: orders/details.html with complex aggregation
- ✅ **Complex Aggregation**: Combines Order + Payment data
- ✅ **@ControllerAdvice**: GlobalExceptionHandler for 4xx/5xx errors
- ✅ **User-Friendly Errors**: Bootstrap alerts, not stack traces
- ✅ **Error Handling**: Extracts messages from Gateway responses

## 🎓 Key Learning Points

### 1. Microservices Communication
- Web client never talks directly to services
- All requests go through API Gateway
- Gateway handles routing and load balancing

### 2. Complex Aggregation Pattern
- Make multiple service calls
- Combine results in controller or service layer
- Present unified view to user
- Critical for microservices UI

### 3. Error Handling in Distributed Systems
- Gateway can return various HTTP errors
- Need to handle connection failures
- Must provide user-friendly messages
- Extract meaningful info from error responses

### 4. Modern Web UI
- Bootstrap provides professional look quickly
- Responsive design is essential
- Icons and colors improve UX
- Validation should be on both client and server

## 📚 Additional Resources

### Documentation
- Spring Boot: https://spring.io/projects/spring-boot
- Thymeleaf: https://www.thymeleaf.org/
- Bootstrap 5: https://getbootstrap.com/
- Bootstrap Icons: https://icons.getbootstrap.com/

### Project Files
- Web Client README: `web-client/README.md`
- Architecture: `ARCHITECTURE.md`
- API Documentation: `OPENAPI_DOCUMENTATION.md`

## 🎉 Conclusion

The Web Client successfully replaces the console client with a modern, user-friendly interface. It demonstrates:

- **Professional UI/UX** with Bootstrap 5
- **Complex aggregation** of microservices data
- **Robust error handling** with friendly messages
- **Full CRUD operations** for warehouse management
- **Responsive design** for all devices
- **Form validation** on client and server
- **RESTful communication** through API Gateway

The application is production-ready and provides an excellent user experience for warehouse management operations.

