# Warehouse Management Web Client

A modern web interface for the Warehouse Management System built with Spring Boot, Thymeleaf, and Bootstrap 5.

## 🚀 Features

- **Modern UI/UX**: Built with Bootstrap 5 for a responsive and intuitive design
- **Complete CRUD Operations**: View, create, and manage warehouse orders
- **Real-time Data**: Fetches data from microservices through the API Gateway
- **Complex Aggregation**: Order details page combines data from Order and Payment services
- **Error Handling**: User-friendly error messages with helpful troubleshooting tips
- **Form Validation**: Both frontend (HTML5) and backend (Spring Validation) validation

## 🏗️ Architecture

The web client communicates with backend microservices through a single API Gateway:

```
Web Client (Port 8090)
    ↓
API Gateway (Port 8080)
    ↓
┌─────────────┬──────────────┬─────────────────┐
│   Place     │    Order     │    Payment      │
│  Service    │   Service    │    Service      │
└─────────────┴──────────────┴─────────────────┘
```

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- All microservices running (Discovery, Gateway, Place, Order, Payment)
- API Gateway accessible at `http://localhost:8080`

## 🔧 Configuration

The web client is configured in `src/main/resources/application.properties`:

```properties
# Server runs on port 8090
server.port=8090

# API Gateway URL
gateway.url=http://localhost:8080

# Thymeleaf configuration
spring.thymeleaf.cache=false
```

## 🚀 Running the Web Client

### Option 1: Using Startup Script (Recommended)

**Windows:**
```bash
start-web-client.bat
```

**Linux/Mac:**
```bash
chmod +x start-web-client.sh
./start-web-client.sh
```

### Option 2: Using Maven

```bash
cd web-client
mvn spring-boot:run
```

### Option 3: Build and Run JAR

```bash
cd web-client
mvn clean package
java -jar target/web-client-1.0-SNAPSHOT.jar
```

## 🌐 Accessing the Application

Once started, open your browser and navigate to:

**http://localhost:8090**

## 📱 Application Pages

### 1. Home Page (`/`)
- Welcome page with navigation cards
- Quick access to all features
- System status indicator

### 2. Free Places (`/places/free`)
- View all available warehouse places
- See place details: ID, section, type, dimensions, price
- Color-coded place types (Standard, Cold, Hazardous)

### 3. Create Order (`/orders/create`)
- Form with HTML5 validation
- Fields:
  - User ID (required)
  - Number of Racks (required, min: 1)
  - Place Type (Standard/Cold/Hazardous)
  - Start Date (required)
  - End Date (required, must be after start date)
- Client-side date validation
- Server-side validation with error messages

### 4. Orders List (`/orders`)
- View all orders
- Order details: ID, user, type, rack count, period, status
- Click "View" to see order details

### 5. Order Details (`/orders/{orderId}`) - **Complex Aggregation**
- **Critical Feature**: Combines data from two services
  - Order information from Order Service
  - Payment information from Payment Service
- Displays:
  - Order details (ID, user, dates, racks, status)
  - Payment status (paid/unpaid)
  - List of payments with amounts and dates
  - Actions: Confirm order, view payments
- Visual indicators for payment status

### 6. Payments (`/payments/order/{orderId}`)
- View all payments for a specific order
- Payment details: ID, amount, date

## 🎨 UI/UX Features

### Bootstrap 5 Components
- Responsive navbar with active state indicators
- Cards with hover effects
- Tables with hover states
- Color-coded badges for status and types
- Alert messages for success/error feedback
- Form validation with visual feedback

### Color Scheme
- **Primary (Blue)**: Main actions, navigation
- **Success (Green)**: Confirmed orders, successful operations, payments
- **Warning (Yellow)**: Pending orders, warnings
- **Info (Light Blue)**: Informational messages, cold storage
- **Danger (Red)**: Errors, critical actions

### Icons
- Bootstrap Icons library
- Intuitive visual indicators
- Consistent icon usage across the application

## 🛡️ Error Handling

The application includes comprehensive error handling:

### Global Exception Handler (`@ControllerAdvice`)
Handles errors from the API Gateway and provides user-friendly messages:

- **4xx Client Errors**:
  - 400 Bad Request: "Invalid data. Please check your input."
  - 404 Not Found: "The requested resource was not found."
  - 409 Conflict: "Not enough places" or other business logic errors

- **5xx Server Errors**:
  - 500+ errors: "Service temporarily unavailable."

- **Connection Errors**:
  - Gateway down: "Unable to connect to the warehouse service."

### Error Page Features
- User-friendly error messages
- Error code display
- Helpful troubleshooting tips
- Navigation options (Go Home, Go Back)

## 🔍 Complex Aggregation Example

The Order Details page (`/orders/{orderId}`) demonstrates complex aggregation:

```java
// Step 1: Fetch order from Order Service
OrderDTO order = warehouseService.getOrderById(orderId);

// Step 2: Fetch payments from Payment Service
List<PaymentDTO> payments = warehouseService.getPaymentsByOrderId(orderId);

// Step 3: Combine into aggregated DTO
OrderDetailsDTO orderDetails = new OrderDetailsDTO(order, payments);
```

This approach:
- Makes two separate API calls through the Gateway
- Combines data from different microservices
- Presents unified view to the user
- Shows payment status based on payment existence

## 📦 Project Structure

```
web-client/
├── src/
│   ├── main/
│   │   ├── java/org/example/webclient/
│   │   │   ├── WebClientApplication.java    # Main application
│   │   │   ├── config/
│   │   │   │   └── RestTemplateConfig.java  # HTTP client config
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java      # Home page
│   │   │   │   ├── PlaceController.java     # Places management
│   │   │   │   ├── OrderController.java     # Orders management
│   │   │   │   └── PaymentController.java   # Payments view
│   │   │   ├── dto/
│   │   │   │   ├── PlaceDTO.java            # Place data
│   │   │   │   ├── OrderDTO.java            # Order data
│   │   │   │   ├── PaymentDTO.java          # Payment data
│   │   │   │   └── OrderDetailsDTO.java     # Aggregated data
│   │   │   ├── service/
│   │   │   │   └── WarehouseWebService.java # API client
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java # Error handling
│   │   └── resources/
│   │       ├── application.properties        # Configuration
│   │       └── templates/                    # Thymeleaf templates
│   │           ├── index.html                # Home page
│   │           ├── error.html                # Error page
│   │           ├── places/
│   │           │   └── free.html             # Free places
│   │           ├── orders/
│   │           │   ├── create.html           # Create order form
│   │           │   ├── list.html             # Orders list
│   │           │   └── details.html          # Order details (aggregation)
│   │           └── payments/
│   │               └── list.html             # Payments list
├── pom.xml
└── README.md
```

## 🔧 Technology Stack

- **Java 17**: Programming language
- **Spring Boot 3.1.5**: Application framework
- **Spring MVC**: Web framework
- **Thymeleaf**: Template engine
- **Bootstrap 5.3.2**: CSS framework
- **Bootstrap Icons**: Icon library
- **Jackson**: JSON processing
- **RestTemplate**: HTTP client

## 📝 API Endpoints Used

The web client consumes the following Gateway endpoints:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/places/free` | GET | Get free places |
| `/api/orders` | POST | Create new order |
| `/api/orders/date-range` | GET | Get orders by date range |
| `/api/orders/{id}` | GET | Get order by ID |
| `/api/orders/{id}/confirm` | PUT | Confirm order |
| `/api/payments/order/{orderId}` | GET | Get payments for order |

## 🐛 Troubleshooting

### Web Client Won't Start
- Check if port 8090 is already in use
- Ensure Java 17+ is installed: `java -version`
- Verify Maven is installed: `mvn -version`

### Can't Connect to Services
- Ensure API Gateway is running at `http://localhost:8080`
- Check that all microservices are registered with Eureka
- Verify Gateway configuration

### Errors When Creating Orders
- Check if Place Service has free places
- Ensure dates are valid (end date after start date)
- Verify all required fields are filled

### No Data Displayed
- Check browser console for errors
- Verify services are returning data
- Check Gateway logs for routing issues

## 🎯 Key Features Implemented

✅ **Service Layer**: `WarehouseWebService` handles all HTTP requests to Gateway  
✅ **Controllers**: Separate controllers for Home, Places, Orders, Payments  
✅ **Thymeleaf Views**: Complete set of templates with Bootstrap 5  
✅ **Form Validation**: Required fields, date validation, type validation  
✅ **Complex Aggregation**: Order Details combines Order + Payment data  
✅ **Error Handling**: `@ControllerAdvice` with user-friendly messages  
✅ **Bootstrap 5 UI**: Modern, responsive, intuitive design  
✅ **No StackTrace**: All errors show friendly messages, not technical details  

## 📄 License

This project is part of the Warehouse Management System.

## 👥 Support

For issues or questions, please check:
1. Server logs in the terminal
2. Browser console for frontend errors
3. API Gateway logs for routing issues
4. Individual microservice logs for business logic errors

