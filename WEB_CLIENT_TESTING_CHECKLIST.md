# Web Client Testing Checklist

Use this checklist to verify that all features of the Web Client are working correctly.

---

## 🔧 Pre-Testing Setup

### Required Services
Ensure these services are running before testing:

- [ ] Discovery Service (Eureka) - Port 8761
- [ ] API Gateway - Port 8080
- [ ] Place Service - Port 8081
- [ ] Order Service - Port 8082
- [ ] Payment Service - Port 8083
- [ ] **Web Client - Port 8090** ← To be tested

### Verification
- [ ] Check Eureka Dashboard: http://localhost:8761
- [ ] Verify all services are registered
- [ ] Check Gateway health: http://localhost:8080/actuator/health (if enabled)

---

## ✅ Feature Testing

### 1. Application Startup
- [ ] Web client starts without errors
- [ ] Console shows: "Started WebClientApplication in X seconds"
- [ ] No error messages in console
- [ ] Port 8090 is bound successfully

### 2. Home Page (`/`)
- [ ] Navigate to http://localhost:8090
- [ ] Page loads successfully
- [ ] Beautiful gradient background visible
- [ ] Four navigation cards displayed:
  - [ ] View Orders (blue icon)
  - [ ] Free Places (green icon)
  - [ ] Create Order (orange icon)
  - [ ] System Info (purple icon)
- [ ] All cards are clickable and navigate correctly
- [ ] Footer displays copyright message
- [ ] System Info shows "Online" badge

### 3. Navigation Bar
- [ ] Navbar visible on all pages
- [ ] Logo/brand name clickable (returns to home)
- [ ] Navigation links work:
  - [ ] Home
  - [ ] Orders
  - [ ] Free Places
  - [ ] New Order
- [ ] Active page is highlighted in navbar
- [ ] Navbar is responsive (test on small screen)

### 4. Free Places Page (`/places/free`)
**Test Case: View Available Places**

- [ ] Navigate to Free Places page
- [ ] Page title: "Free Warehouse Places"
- [ ] Badge shows count: "X Available"
- [ ] Table displays if places exist:
  - [ ] Rack ID column
  - [ ] Section column (badge format)
  - [ ] Number column
  - [ ] Type column (color-coded badge)
  - [ ] Dimensions column (W×H×D format)
  - [ ] Price per Day column
  - [ ] Status column (green badge)
- [ ] Color coding is correct:
  - [ ] STANDARD = Blue
  - [ ] COLD = Cyan
  - [ ] HAZARDOUS = Orange/Yellow
- [ ] Info card at bottom explains place types
- [ ] Table is responsive (scrolls horizontally if needed)
- [ ] Empty state message if no places available

### 5. Create Order Form (`/orders/create`)
**Test Case 5.1: Display Form**

- [ ] Navigate to Create Order page
- [ ] Form displays with all fields:
  - [ ] User ID (text input)
  - [ ] Number of Racks (number input)
  - [ ] Place Type (dropdown select)
  - [ ] Start Date (date picker)
  - [ ] End Date (date picker)
- [ ] All fields marked with red asterisk (*)
- [ ] Dropdown has options: Standard, Cold, Hazardous
- [ ] Help text visible at bottom

**Test Case 5.2: Frontend Validation**

Try submitting with empty fields:
- [ ] User ID required error shows
- [ ] Rack count required error shows
- [ ] Place type required error shows
- [ ] Start date required error shows
- [ ] End date required error shows

Try invalid rack count:
- [ ] Enter 0 or negative → Error: "must be at least 1"

Try invalid dates:
- [ ] End date before start date → Error shown
- [ ] JavaScript validation prevents submission

**Test Case 5.3: Valid Order Creation**

- [ ] Fill all fields correctly:
  - User ID: `USER-001`
  - Rack Count: `2`
  - Place Type: `STANDARD`
  - Start Date: Today or future
  - End Date: After start date
- [ ] Click "Create Order"
- [ ] Success message appears
- [ ] Redirected to order details page
- [ ] Order ID is visible

**Test Case 5.4: Backend Validation**

- [ ] Try to bypass frontend validation (use browser dev tools)
- [ ] Backend validation catches invalid data
- [ ] Appropriate error message displayed

**Test Case 5.5: Error Handling**

- [ ] Stop Place Service
- [ ] Try to create order
- [ ] User-friendly error message appears
- [ ] Error page shows troubleshooting tips
- [ ] "Go Back" button works

### 6. Orders List Page (`/orders`)
**Test Case: View All Orders**

- [ ] Navigate to Orders page
- [ ] Page title: "All Orders"
- [ ] Badge shows count: "X Orders"
- [ ] "New Order" button visible and clickable
- [ ] Table displays if orders exist:
  - [ ] Order ID column
  - [ ] User ID column
  - [ ] Type column (color-coded)
  - [ ] Racks column (badge)
  - [ ] Period column (dates)
  - [ ] Status column (color-coded)
  - [ ] Actions column (View button)
- [ ] Status colors correct:
  - [ ] PENDING = Yellow
  - [ ] CONFIRMED = Green
  - [ ] CANCELLED = Gray
- [ ] "View" buttons are clickable
- [ ] Info card explains status legend
- [ ] Empty state message if no orders

### 7. Order Details Page (`/orders/{orderId}`) ⭐ **CRITICAL**
This is the **Complex Aggregation** feature - most important test!

**Test Case 7.1: View Order Details**

- [ ] Click "View" on an order from the list
- [ ] Redirected to order details page
- [ ] Breadcrumb navigation visible
- [ ] Page title: "Order Details"
- [ ] Status badge displayed (green/yellow/gray)

**Test Case 7.2: Order Information Card (Blue Header)**

- [ ] Card displays with blue header
- [ ] Order information shown:
  - [ ] Order ID
  - [ ] User ID
  - [ ] Place Type (badge)
  - [ ] Number of Racks (badge)
  - [ ] Start Date (with icon)
  - [ ] End Date (with icon)
  - [ ] Assigned Racks (if any, as badges)
- [ ] All data matches the order

**Test Case 7.3: Payment Information Card ⭐ COMPLEX AGGREGATION**

**If order has NO payments:**
- [ ] Card header is YELLOW/WARNING color
- [ ] Header shows "Not Paid" with warning icon
- [ ] Warning alert visible: "No payments found for this order"
- [ ] Message explains payment is required

**If order HAS payments:**
- [ ] Card header is GREEN/SUCCESS color
- [ ] Header shows "Paid" with check icon
- [ ] Success alert visible: "Payment received!"
- [ ] Table displays all payments:
  - [ ] Payment ID
  - [ ] Amount (green badge with $)
  - [ ] Date (formatted nicely)
- [ ] All payment data is correct

**Test Case 7.4: Complex Aggregation Verification**

- [ ] Order data comes from Order Service
- [ ] Payment data comes from Payment Service
- [ ] Both displayed on SAME page
- [ ] Info box at bottom explains aggregation
- [ ] No errors in browser console
- [ ] Page loads reasonably fast

**Test Case 7.5: Actions**

- [ ] "View Payments" button visible and clickable
- [ ] "Back to Orders" button visible and clickable
- [ ] If order is PENDING:
  - [ ] "Confirm Order" button visible
  - [ ] Click confirm → Confirmation dialog appears
  - [ ] Confirm → Order status changes to CONFIRMED
  - [ ] Success message appears
  - [ ] Page refreshes with updated status

### 8. Payments Page (`/payments/order/{orderId}`)
**Test Case: View Payments for Order**

- [ ] From order details, click "View Payments"
- [ ] Redirected to payments page
- [ ] Breadcrumb shows navigation path
- [ ] Page title shows order ID
- [ ] Badge shows payment count
- [ ] If payments exist:
  - [ ] Table displays all payments
  - [ ] Payment ID, Order ID, User ID, Amount, Date shown
  - [ ] Order ID is clickable link
- [ ] If no payments:
  - [ ] Warning message displayed
- [ ] "Back to Order Details" button works

### 9. Error Handling ⭐ **CRITICAL**
**Test Case 9.1: Gateway Down**

- [ ] Stop API Gateway
- [ ] Try to access any page (e.g., Free Places)
- [ ] Error page appears (not stack trace!)
- [ ] Error title: "Connection Error"
- [ ] Message: "Unable to connect to warehouse service"
- [ ] Troubleshooting tips visible
- [ ] "Go Home" and "Go Back" buttons work

**Test Case 9.2: Service Unavailable**

- [ ] Stop Order Service
- [ ] Try to create an order or view orders
- [ ] User-friendly error message appears
- [ ] No stack trace visible
- [ ] Bootstrap alert styling

**Test Case 9.3: Not Found**

- [ ] Navigate to non-existent order: http://localhost:8090/orders/FAKE-999
- [ ] Error page appears
- [ ] Error title: "Not Found"
- [ ] User-friendly message

**Test Case 9.4: Invalid Data**

- [ ] Try to create order with invalid data (if backend validation fails)
- [ ] Appropriate error message shown
- [ ] Error extracted from Gateway response

**Test Case 9.5: Not Enough Places**

- [ ] Create many orders to exhaust available places
- [ ] Try to create another order
- [ ] Error message: "Not enough places" or similar
- [ ] User-friendly presentation (no stack trace)

### 10. Responsive Design
**Test Case: Mobile/Tablet View**

- [ ] Resize browser to mobile size (375px width)
- [ ] Navbar collapses to hamburger menu
- [ ] Hamburger menu works (expands/collapses)
- [ ] Cards stack vertically on home page
- [ ] Tables scroll horizontally if needed
- [ ] Forms are still usable
- [ ] Buttons are tappable (not too small)
- [ ] Text is readable

### 11. Bootstrap Styling
**Test Case: UI/UX Elements**

- [ ] Colors are consistent
- [ ] Icons are visible and appropriate
- [ ] Badges are styled correctly
- [ ] Buttons have hover effects
- [ ] Cards have hover effects on home page
- [ ] Tables have hover effects on rows
- [ ] Alerts are dismissible (X button works)
- [ ] Forms have proper spacing
- [ ] Footer is at bottom of page

### 12. Navigation Flow
**Test Case: User Journey**

Complete this journey without errors:
- [ ] Start at home page
- [ ] Click "Free Places" → View places
- [ ] Click "New Order" in navbar
- [ ] Fill form and create order
- [ ] View success message
- [ ] See order details (complex aggregation)
- [ ] Click "View Payments"
- [ ] See payment information
- [ ] Click "Back to Order Details"
- [ ] Click "Back to Orders"
- [ ] See order in list
- [ ] Click "Home" in navbar
- [ ] Return to home page

All navigation should be smooth with no errors!

---

## 🐛 Bug Testing

### Known Issues to Check
- [ ] Date picker works in all browsers
- [ ] Forms submit correctly in Firefox, Chrome, Edge
- [ ] Templates render correctly (no broken layouts)
- [ ] No console errors in browser dev tools
- [ ] No exceptions in server console

### Edge Cases
- [ ] What happens with very long order IDs?
- [ ] What happens with many (100+) orders in list?
- [ ] What happens with special characters in User ID?
- [ ] What happens if Gateway returns malformed JSON?
- [ ] What happens if services are slow (10+ seconds)?

---

## 📊 Performance Testing

### Load Times
- [ ] Home page loads in < 2 seconds
- [ ] Free places page loads in < 3 seconds
- [ ] Order list page loads in < 3 seconds
- [ ] Order details page loads in < 4 seconds (2 API calls)
- [ ] Form submission completes in < 5 seconds

### Resource Usage
- [ ] Memory usage is reasonable (< 500MB)
- [ ] CPU usage is low when idle
- [ ] No memory leaks (check after extended use)

---

## ✅ Final Verification

### Requirement Checklist
- [ ] Java 17+ in use
- [ ] Spring Boot 3.x in use
- [ ] Spring MVC controllers implemented
- [ ] Thymeleaf templates working
- [ ] Bootstrap 5 styling applied
- [ ] RestTemplate configured and used
- [ ] Port 8090 (no conflict)
- [ ] Service layer (WarehouseWebService) working
- [ ] Multiple controllers (Home, Place, Order, Payment)
- [ ] Multiple views with navigation
- [ ] Free Places page working
- [ ] Create Order form with validation working
- [ ] **Complex Aggregation working** ⭐
- [ ] @ControllerAdvice handling errors
- [ ] User-friendly error messages (no stack traces)

### Critical Features
- [ ] ✅ Complex Aggregation (Order + Payment data)
- [ ] ✅ Form Validation (frontend + backend)
- [ ] ✅ Error Handling (user-friendly messages)
- [ ] ✅ Bootstrap 5 UI (modern design)
- [ ] ✅ All CRUD operations work

---

## 🎯 Testing Summary

After completing this checklist, you should have verified:

1. ✅ All pages load correctly
2. ✅ Navigation works smoothly
3. ✅ Forms work with validation
4. ✅ Complex aggregation works
5. ✅ Error handling is user-friendly
6. ✅ UI is modern and responsive
7. ✅ All requirements are met

---

## 📝 Test Report Template

After testing, document your results:

```
WEB CLIENT TEST REPORT
Date: ______________
Tester: ______________

Startup: [ PASS / FAIL ]
Home Page: [ PASS / FAIL ]
Navigation: [ PASS / FAIL ]
Free Places: [ PASS / FAIL ]
Create Order: [ PASS / FAIL ]
Orders List: [ PASS / FAIL ]
Order Details (Complex Aggregation): [ PASS / FAIL ]
Payments: [ PASS / FAIL ]
Error Handling: [ PASS / FAIL ]
Responsive Design: [ PASS / FAIL ]

Issues Found:
1. 
2. 
3. 

Overall Status: [ PASS / FAIL ]
```

---

## 🎉 Testing Complete

If all tests pass, the Web Client is ready for use!

**Good luck with your testing!** 🚀

