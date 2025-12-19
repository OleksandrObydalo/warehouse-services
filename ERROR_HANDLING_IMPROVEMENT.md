# 🔧 Error Handling Improvement

## 📋 Overview

Improved error handling to provide clear, user-friendly error messages when operations fail. Now users will see specific error messages instead of generic "Invalid Request" messages.

## 🎯 Problems Solved

### Before
- ❌ "Invalid Request" - unclear what went wrong
- ❌ "Error Code: 400" - not helpful for users
- ❌ Generic messages that don't explain the actual problem

### After
- ✅ "Not enough free places available. Required: 3, Available: 2"
- ✅ "Order cannot be confirmed without payment"
- ✅ "Not enough free places available" (when confirming)
- ✅ Clear, actionable error messages

## 📝 Changes Made

### 1. Web Client - Enhanced Error Message Extraction

**File:** `web-client/src/main/java/org/example/webclient/exception/GlobalExceptionHandler.java`

#### Improvements:
- **Better JSON parsing**: Now extracts both `"message"` and `"error"` fields from JSON responses
- **Plain text support**: Handles non-JSON error responses
- **Character unescaping**: Properly handles escaped characters (`\"`, `\n`, `\r`, `\t`)
- **Debug logging**: Added logging to help troubleshoot error extraction
- **Null handling**: Returns `null` when extraction fails, allowing fallback to default messages

#### Key Changes:

```java
private String extractErrorMessage(HttpClientErrorException ex, String defaultMessage) {
    String responseBody = ex.getResponseBodyAsString();
    
    // Extract from "message" field
    if (responseBody.contains("\"message\"")) {
        // ... extract and unescape message
    }
    
    // Extract from "error" field
    if (responseBody.contains("\"error\"")) {
        // ... extract and unescape error
    }
    
    // Handle plain text responses
    if (!responseBody.trim().startsWith("{")) {
        return responseBody.trim();
    }
    
    return defaultMessage;
}
```

### 2. Order Service - Consistent Error Response Format

**File:** `order-service/src/main/java/org/example/orderservice/exception/GlobalExceptionHandler.java` (NEW)

#### Purpose:
Ensures all errors from the Order Service are returned in a consistent JSON format that the web client can parse.

#### Features:
- **Consistent JSON format**: All errors use the same structure
- **Proper HTTP status codes**: 400 for business logic errors, 500 for unexpected errors
- **Detailed error messages**: Preserves the detailed error messages from the service layer
- **Timestamp and status**: Includes timestamp and HTTP status in response

#### Error Response Format:

```json
{
  "timestamp": "2025-12-19T09:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Not enough free places available. Required: 3, Available: 2"
}
```

#### Exception Handling:

1. **ValidationException** (400 Bad Request)
   - Schema validation errors
   - Input validation failures

2. **RuntimeException** (400 Bad Request)
   - Business logic errors (e.g., not enough places)
   - State transition errors (e.g., cannot confirm unpaid order)

3. **General Exception** (500 Internal Server Error)
   - Unexpected errors
   - System failures

## 🧪 Testing Scenarios

### Scenario 1: Not Enough Racks Available (Create Order)

**Steps:**
1. Go to "Free Places" - see only 2 refrigerated racks available
2. Go to "Create Order"
3. Select "Cold Storage (Refrigerated)"
4. Enter "3" for Number of Racks
5. Select dates and submit

**Expected Result:**
```
Invalid Request
Error Code: 400

Not enough free places available. Required: 3, Available: 2
```

### Scenario 2: Confirm Order Without Payment

**Steps:**
1. Create a new order
2. Go to order details
3. Click "Confirm Order" (without adding payment)

**Expected Result:**
```
Invalid Request
Error Code: 400

Order cannot be confirmed without payment
```

### Scenario 3: Confirm Order Without Available Racks

**Steps:**
1. Create an order and add payment
2. Meanwhile, another user rents all available racks
3. Try to confirm your order

**Expected Result:**
```
Invalid Request
Error Code: 400

Not enough free places available
```

### Scenario 4: Invalid State Transition

**Steps:**
1. Create and confirm an order
2. Try to confirm it again

**Expected Result:**
```
Invalid Request
Error Code: 400

Order cannot be confirmed. Current status: CONFIRMED
```

## 📊 Error Message Examples

### Order Creation Errors

| Scenario | Error Message |
|----------|---------------|
| Not enough racks | "Not enough free places available. Required: 3, Available: 2" |
| Invalid dates | "End date must be after start date" |
| Invalid rack type | "Invalid rack type specified" |

### Order Confirmation Errors

| Scenario | Error Message |
|----------|---------------|
| No payment | "Order cannot be confirmed without payment" |
| No racks available | "Not enough free places available" |
| Wrong status | "Order cannot be confirmed. Current status: CONFIRMED" |

### Order State Transition Errors

| Scenario | Error Message |
|----------|---------------|
| Cannot start | "Order cannot be started. Current status: NEW" |
| Cannot finish | "Order cannot be finished. Current status: CONFIRMED" |
| Cannot cancel | "Order cannot be cancelled. Current status: ACTIVE" |

## 🔍 How It Works

### Request Flow:

```
User Action → Web Client → API Gateway → Order Service
                                              ↓
                                         Error Occurs
                                              ↓
                                    GlobalExceptionHandler
                                              ↓
                                    JSON Error Response
                                              ↓
API Gateway ← Web Client ← Extract Message ←┘
     ↓
Error Page with Clear Message
```

### Error Extraction Process:

1. **Order Service** throws `RuntimeException` with detailed message
2. **GlobalExceptionHandler** catches it and formats as JSON:
   ```json
   {
     "message": "Not enough free places available. Required: 3, Available: 2"
   }
   ```
3. **API Gateway** forwards the error response
4. **Web Client** receives `HttpClientErrorException`
5. **GlobalExceptionHandler** (web client) extracts the message
6. **Error page** displays the clear, user-friendly message

## ✅ Benefits

### For Users:
- ✅ Clear understanding of what went wrong
- ✅ Actionable information (e.g., "Required: 3, Available: 2")
- ✅ No confusing technical jargon
- ✅ Better user experience

### For Developers:
- ✅ Consistent error format across all services
- ✅ Easy to debug with detailed messages
- ✅ Centralized error handling
- ✅ Proper HTTP status codes

## 🚀 How to Test

### Step 1: Restart Order Service

```bash
# Stop the current Order Service
# Restart it to load the new GlobalExceptionHandler
cd order-service
mvn spring-boot:run
```

### Step 2: Restart Web Client

```bash
# Stop the current Web Client
# Restart it to load the improved error handling
cd web-client
mvn spring-boot:run
```

### Step 3: Test Error Scenarios

1. **Test "Not Enough Racks":**
   - Check available refrigerated racks
   - Try to order more than available
   - Verify you see: "Not enough free places available. Required: X, Available: Y"

2. **Test "Confirm Without Payment":**
   - Create an order
   - Try to confirm without payment
   - Verify you see: "Order cannot be confirmed without payment"

3. **Test "Invalid State Transition":**
   - Confirm an order
   - Try to confirm it again
   - Verify you see: "Order cannot be confirmed. Current status: CONFIRMED"

## 📚 Files Modified

### Web Client
- `web-client/src/main/java/org/example/webclient/exception/GlobalExceptionHandler.java`
  - Enhanced `extractErrorMessage()` method
  - Better JSON parsing
  - Plain text support
  - Character unescaping

### Order Service
- `order-service/src/main/java/org/example/orderservice/exception/GlobalExceptionHandler.java` (NEW)
  - Consistent JSON error format
  - Proper exception handling
  - HTTP status code management

## 🎉 Result

Users now see clear, actionable error messages that help them understand what went wrong and how to fix it!

### Example:

**Before:**
```
Invalid Request
Error Code: 400
The request contains invalid data. Please check your input.
```

**After:**
```
Invalid Request
Error Code: 400
Not enough free places available. Required: 3, Available: 2
```

---

**Error handling is now user-friendly and informative!** 🎉

