# Web Client - Authentication & Access Control Guide

## 🔐 Authentication System Implemented

A complete session-based authentication system has been added to the web client with the following features:

### ✅ Features Implemented

1. **Login System** - Username/password authentication
2. **Session Management** - User info stored in HTTP session
3. **Access Control** - Users can only view/modify their own data
4. **Auto User ID** - No need to enter user ID manually
5. **Logout Functionality** - Secure session termination
6. **Protected Pages** - All pages require login

---

## 🚀 How It Works

### Login Flow
```
1. User visits any page → Redirected to /login
2. User enters username & password
3. System authenticates credentials
4. On success: Session created, redirect to home
5. On failure: Error message, stay on login page
```

### Access Control
```
- Users can ONLY view their own:
  ✓ Orders
  ✓ Payments
  ✓ Rented Places
  
- Attempting to access another user's data:
  ✗ Error: "Access denied"
```

---

## 👤 Test User Accounts

| Username | Password | User ID |
|----------|----------|---------|
| user1    | u001     | u001    |
| user2    | u002     | u002    |
| user3    | u003     | u003    |
| admin    | admin    | admin   |

**Note:** For simplicity, password = userId. In production, use proper password hashing!

---

## 📁 New Files Created

### Backend Components (5 files)
1. `UserDTO.java` - User data transfer object
2. `AuthenticationService.java` - In-memory user authentication
3. `AuthController.java` - Login/logout endpoints
4. `AuthenticationInterceptor.java` - Session check before page access
5. `WebConfig.java` - Register interceptor

### Frontend Templates (1 file)
1. `auth/login.html` - Beautiful login page with Bootstrap 5

---

## 🔧 Controllers Updated

### OrderController
- ✅ `listOrders()` - Shows only logged-in user's orders
- ✅ `showCreateOrderForm()` - Pre-fills user ID from session
- ✅ `createOrder()` - Forces order creation for logged-in user only
- ✅ `showOrderDetails()` - Checks order ownership before displaying
- ✅ `confirmOrder()` - Verifies user owns order
- ✅ `cancelOrder()` - Verifies user owns order
- ✅ `startOrder()` - Verifies user owns order
- ✅ `finishOrder()` - Verifies user owns order
- ✅ `showAddPaymentForm()` - Checks order ownership
- ✅ `addPayment()` - Checks order ownership

### PlaceController
- ✅ `showUserPlaces()` - Only shows logged-in user's places
- ✅ `showMyPlaces()` - NEW: Convenience redirect to own places

### PaymentController
- ✅ `showPaymentsByOrder()` - Checks order ownership before showing payments

---

## 🎨 UI Changes

### Navigation Bar
**Before:**
```
[Home] [Orders] [Free Places] [My Places ▼] [New Order]
```

**After:**
```
[Home] [Orders] [Free Places] [My Places] [New Order] [👤 Username ▼]
                                                            └─ User ID: u001
                                                            └─ Logout
```

### Login Page
- Beautiful blue gradient background
- Username & password fields
- Test credentials displayed
- Success/error messages
- Bootstrap 5 styling

### Create Order Form
- User ID field is now **hidden** (auto-filled)
- User can't create orders for other users

---

## 🔒 Security Features

### 1. Session-Based Authentication
```java
// User info stored in HTTP session
session.setAttribute("userId", user.getUserId());
session.setAttribute("username", user.getUsername());
```

### 2. Interceptor Protection
```java
// All pages require login (except /login, /logout, static resources)
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    // Redirects to /login if not authenticated
}
```

### 3. Ownership Verification
```java
// Helper method in OrderController
private void checkOrderOwnership(String orderId, String loggedInUserId) {
    OrderDTO order = warehouseService.getOrderById(orderId);
    if (!order.getUserId().equals(loggedInUserId)) {
        throw new RuntimeException("Access denied");
    }
}
```

### 4. Automatic User ID Assignment
```java
// In createOrder()
String loggedInUserId = (String) session.getAttribute("userId");
orderDTO.setUserId(loggedInUserId); // Force user's own ID
```

---

## 🧪 Testing Scenarios

### Test 1: Login
1. Go to http://localhost:8091
2. Should redirect to /login
3. Enter: username=`user1`, password=`u001`
4. Click Login
5. Should redirect to home page
6. Navigation shows "user1" with dropdown

### Test 2: View Own Orders
1. Login as `user1`
2. Go to Orders
3. Should see only orders for user `u001`
4. Other users' orders are hidden

### Test 3: Create Order
1. Login as `user1`
2. Click "New Order"
3. Notice: User ID field is hidden/pre-filled
4. Fill form and submit
5. Order is created for `u001` automatically

### Test 4: Access Control
1. Login as `user1` (u001)
2. Try to access order belonging to `user2`:
   - URL: `/orders/{order-id-of-user2}`
3. Should see error: "Access denied: You can only view your own orders"

### Test 5: View My Places
1. Login as `user1`
2. Click "My Places" in navigation
3. Should see places rented by `u001`
4. Try URL: `/places/user/u002`
5. Should see error: "Access denied"

### Test 6: Logout
1. Click username dropdown
2. Click "Logout"
3. Session cleared
4. Redirected to login page
5. Try to access any page → Redirected to login

---

## 🔄 Session Management

### Session Attributes
```java
session.getAttribute("userId")    // User's ID (e.g., "u001")
session.getAttribute("username")  // User's display name (e.g., "user1")
```

### Session Lifecycle
- **Created:** On successful login
- **Used:** Every request to check authentication & ownership
- **Destroyed:** On logout or session timeout

---

## 🚨 Error Messages

### Access Denied Errors
- **Order access:** "Access denied: You can only view your own orders"
- **Place access:** "Access denied: You can only view your own places"
- **Payment access:** "Access denied: You can only view payments for your own orders"

### Login Errors
- **Invalid credentials:** "Invalid username or password"

### Success Messages
- **Login:** "Welcome, {username}!"
- **Logout:** "You have been logged out successfully"

---

## 📝 Important Notes

### For Development
- User database is **in-memory** (AuthenticationService)
- Pre-populated with 4 test users
- Password = userId for simplicity
- **Production:** Use real database + BCrypt password hashing

### For Production
1. Replace `AuthenticationService` with database
2. Add password hashing (BCrypt)
3. Add HTTPS
4. Add CSRF protection
5. Add session timeout configuration
6. Add "Remember Me" functionality
7. Add password reset functionality

---

## 🎯 Access Control Matrix

| Action | Own Data | Other User's Data |
|--------|----------|-------------------|
| View Orders | ✅ Allowed | ❌ Denied |
| Create Order | ✅ Allowed | ❌ Denied |
| View Order Details | ✅ Allowed | ❌ Denied |
| Confirm Order | ✅ Allowed | ❌ Denied |
| Cancel Order | ✅ Allowed | ❌ Denied |
| Start Order | ✅ Allowed | ❌ Denied |
| Finish Order | ✅ Allowed | ❌ Denied |
| Add Payment | ✅ Allowed | ❌ Denied |
| View Payments | ✅ Allowed | ❌ Denied |
| View My Places | ✅ Allowed | ❌ Denied |
| View Free Places | ✅ Allowed | ✅ Allowed |

---

## 🔧 Configuration

### Session Timeout (Optional)
Add to `application.properties`:
```properties
# Session timeout (30 minutes)
server.servlet.session.timeout=30m
```

### Excluded Paths
These paths don't require authentication:
- `/login` - Login page
- `/logout` - Logout endpoint
- `/css/**` - Static CSS files
- `/js/**` - Static JavaScript files
- `/images/**` - Static images
- `/error` - Error pages

---

## 📚 Code Examples

### Check if User is Logged In (in Controller)
```java
@GetMapping("/some-page")
public String somePage(HttpSession session, Model model) {
    String userId = (String) session.getAttribute("userId");
    String username = (String) session.getAttribute("username");
    
    // userId and username are guaranteed to be non-null
    // (interceptor ensures user is logged in)
    
    return "some-page";
}
```

### Check Ownership
```java
@GetMapping("/orders/{orderId}")
public String viewOrder(@PathVariable String orderId, 
                       HttpSession session, 
                       Model model) {
    String loggedInUserId = (String) session.getAttribute("userId");
    
    OrderDTO order = warehouseService.getOrderById(orderId);
    
    if (!order.getUserId().equals(loggedInUserId)) {
        throw new RuntimeException("Access denied");
    }
    
    // User owns this order, proceed...
    return "order-details";
}
```

### Access User Info in Thymeleaf
```html
<!-- Show username -->
<span th:text="${session.username}">User</span>

<!-- Show user ID -->
<span th:text="${session.userId}">u001</span>

<!-- Conditional based on user -->
<div th:if="${session.userId == 'admin'}">
    Admin-only content
</div>
```

---

## ✅ Summary

### What Changed
1. ✅ Login page added
2. ✅ Session management implemented
3. ✅ All controllers updated with access control
4. ✅ User ID automatically filled from session
5. ✅ Navigation shows logged-in user
6. ✅ Logout functionality added
7. ✅ Users can only see their own data

### What Users Experience
1. Must login to access system
2. See only their own orders/payments/places
3. Don't need to enter user ID anymore
4. Can logout anytime
5. Clear error messages if trying to access others' data

---

## 🚀 Next Steps

1. **Restart Web Client:**
   ```bash
   cd web-client
   mvn spring-boot:run
   ```

2. **Test Login:**
   - Go to http://localhost:8091
   - Login with: `user1` / `u001`

3. **Test Access Control:**
   - Create order as user1
   - Logout
   - Login as user2
   - Try to access user1's order → Should see error

---

**Authentication system is complete and ready to use!** 🔐

All user data is now protected and users can only access their own information!

