# Web Client - Quick Feature Guide

## 🎯 All New Features at a Glance

---

## Order Lifecycle Management

### 1️⃣ CREATE Order
**Status:** CREATED  
**Actions Available:**
- 💰 **Add Payment** → Opens payment form
- ✅ **Confirm Order** → Assigns places (requires payment!)
- ❌ **Cancel Order** → Cancels the order

**Payment Validation:**
```
❌ No Payment → Confirm button DISABLED
                 Click shows: "⚠️ Payment Required!"
                 
✅ Has Payment → Confirm button ENABLED
                 Click confirms order
```

---

### 2️⃣ CONFIRM Order
**Status:** CONFIRMED  
**Actions Available:**
- ▶️ **Start Order** → Marks order as active
- ❌ **Cancel Order** → Cancels and frees places

---

### 3️⃣ START Order
**Status:** ACTIVE  
**Actions Available:**
- 🔄 **Extend Term (Add Payment)** → Add more payment to extend
- ⏹️ **Finish Order** → Completes order and frees places

---

### 4️⃣ FINISH Order
**Status:** FINISHED  
**Actions Available:**
- ℹ️ No actions (order complete)
- Places automatically freed

---

## 💳 Payment Management

### Add Payment to Order

**When:** Anytime during CREATED or ACTIVE status

**Steps:**
1. Click **"Add Payment"** button
2. Enter amount (e.g., `100.00`)
3. Submit form
4. Payment recorded with current timestamp

**Use Cases:**
- Initial payment before confirmation
- Additional payment to extend active order term
- Multiple payments for long-term orders

---

## 📦 View User Places

**Feature:** See all places currently rented by a user

**Access:**
- From order details: Click **"View My Places"** button
- Direct URL: `/places/user/{userId}`

**Information Shown:**
- Rack ID
- Section & Number
- Type (STANDARD/REFRIGERATED/SECURE)
- Dimensions (W×H×D)
- Price per Day
- Status (OCCUPIED)

---

## 🎨 Visual Guide

### Order Details Page Layout

```
┌─────────────────────────────────────────────────┐
│  📋 Order Details                    [STATUS]   │
├─────────────────────────────────────────────────┤
│                                                  │
│  📦 Order Information Card                      │
│  ├─ Order ID: ORD-xxx                          │
│  ├─ User ID: u001                              │
│  ├─ Type: STANDARD                             │
│  ├─ Dates: 18.12.2025 - 18.01.2026           │
│  └─ Assigned Racks: [RACK-001] [RACK-002]     │
│                                                  │
├─────────────────────────────────────────────────┤
│                                                  │
│  💳 Payment Information Card                    │
│  ├─ Status: ✅ Paid / ⚠️ Not Paid            │
│  └─ Payments List:                             │
│      • PAY-001: $100.00 (17 Dec 2025)         │
│      • PAY-002: $50.00  (18 Dec 2025)         │
│                                                  │
├─────────────────────────────────────────────────┤
│                                                  │
│  ⚙️ Actions (Based on Status)                  │
│                                                  │
│  [CREATED Status]                               │
│  [💰 Add Payment] [✅ Confirm] [❌ Cancel]     │
│                                                  │
│  [CONFIRMED Status]                             │
│  [▶️ Start Order] [❌ Cancel]                  │
│                                                  │
│  [ACTIVE Status]                                │
│  [🔄 Extend Term] [⏹️ Finish]                 │
│                                                  │
│  Common Actions:                                │
│  [💳 View All Payments] [👤 View My Places]   │
│  [⬅️ Back to Orders]                           │
│                                                  │
└─────────────────────────────────────────────────┘
```

---

## 🔄 Complete Flow Example

### Scenario: Rent a warehouse place for 1 month

```
1. CREATE ORDER
   ├─ User: u001
   ├─ Type: STANDARD
   ├─ Count: 1 rack
   └─ Period: 18.12.2025 - 18.01.2026
   
2. ADD PAYMENT
   ├─ Amount: $100.00
   └─ Status: Payment recorded ✅
   
3. CONFIRM ORDER
   ├─ Places assigned: RACK-A-001
   └─ Status: CONFIRMED ✅
   
4. START ORDER
   ├─ Order becomes active
   └─ Status: ACTIVE ✅
   
5. (Optional) EXTEND TERM
   ├─ Add payment: $50.00
   └─ Term extended ✅
   
6. FINISH ORDER
   ├─ Places freed: RACK-A-001
   └─ Status: FINISHED ✅
```

---

## ⚠️ Important Notes

### Payment Validation
- **Cannot confirm** order without payment
- Friendly alert shows instead of error page
- Clear instructions: "Click 'Add Payment' button"

### Order Status Rules
- **CREATED** → Can confirm (if paid) or cancel
- **CONFIRMED** → Can start or cancel
- **ACTIVE** → Can extend or finish
- **FINISHED/CANCELLED** → No actions available

### Place Management
- Places assigned on **CONFIRM**
- Places freed on **CANCEL** (if confirmed)
- Places freed on **FINISH**

---

## 🎯 Quick Actions Reference

| Current Status | Available Actions | Result |
|----------------|-------------------|--------|
| CREATED | Add Payment | Payment recorded |
| CREATED | Confirm (if paid) | → CONFIRMED |
| CREATED | Cancel | → CANCELLED |
| CONFIRMED | Start | → ACTIVE |
| CONFIRMED | Cancel | → CANCELLED |
| ACTIVE | Extend Term | Payment added |
| ACTIVE | Finish | → FINISHED |
| FINISHED | None | Order complete |
| CANCELLED | None | Order cancelled |

---

## 💡 Pro Tips

### 1. Check Payment Before Confirming
Always add payment first, then confirm. The system will prevent confirmation without payment.

### 2. View Your Places Anytime
Click "View My Places" from any order details page to see all your rented places.

### 3. Extend Active Orders
For long-term rentals, add additional payments while order is ACTIVE to extend the term.

### 4. Cancel Early If Needed
You can cancel orders before they start (CREATED or CONFIRMED status) without penalty.

### 5. Finish When Done
Always finish active orders when you're done to free up places for others.

---

## 🚀 Getting Started

1. **Start Web Client:**
   ```bash
   cd web-client
   mvn spring-boot:run
   ```

2. **Access Application:**
   ```
   http://localhost:8091
   ```

3. **Create First Order:**
   - Click "New Order"
   - Fill form
   - Submit

4. **Test All Features:**
   - Add payment
   - Confirm order
   - Start order
   - View places
   - Finish order

---

## ✅ Feature Checklist

Use this to test all features:

- [ ] Create order
- [ ] Try to confirm without payment (see friendly error)
- [ ] Add payment
- [ ] Confirm order
- [ ] View assigned places
- [ ] Start order
- [ ] Add another payment (extend)
- [ ] View all payments
- [ ] View user places
- [ ] Finish order
- [ ] Verify places freed

---

## 📞 Need Help?

See detailed documentation:
- `WEB_CLIENT_NEW_FEATURES.md` - Full feature documentation
- `WEB_CLIENT_GUIDE.md` - Complete implementation guide
- `WEB_CLIENT_TESTING_CHECKLIST.md` - Testing guide

---

**Enjoy the new features!** 🎉

