# HTTP Requests для тестування Warehouse Management System

Цей файл містить приклади HTTP запитів для тестування всіх endpoint'ів мікросервісів.

## Швидкий довідник endpoint'ів

| Сервіс | Метод | Endpoint | Опис |
|--------|-------|----------|------|
| **Place Service** | GET | `/api/places/free` | Отримати всі вільні місця |
| | GET | `/api/places/free/type/{type}` | Отримати вільні місця за типом |
| | GET | `/api/places/user/{userId}` | Отримати місця користувача |
| | POST | `/api/places/give` | Видати місця користувачу |
| | POST | `/api/places/free` | Звільнити місця |
| **Order Service** | POST | `/api/orders` | Створити замовлення |
| | GET | `/api/orders/date-range` | Отримати замовлення за діапазоном дат |
| | PUT | `/api/orders/{orderId}/confirm` | Затвердити замовлення |
| | PUT | `/api/orders/{orderId}/cancel` | Скасувати замовлення |
| | PUT | `/api/orders/{orderId}/start` | Почати замовлення |
| | PUT | `/api/orders/{orderId}/finish` | Завершити замовлення |
| **Payment Service** | POST | `/api/payments` | Створити оплату |
| | GET | `/api/payments` | Отримати всі оплати |
| | GET | `/api/payments/order/{orderId}` | Отримати оплати за замовленням |
| | GET | `/api/payments/user/{userId}` | Отримати оплати користувача |

## Передумови

1. Запустіть всі мікросервіси (Place Service, Order Service, Payment Service)
2. Переконайтеся, що сервіси доступні на портах:
   - Place Service: http://localhost:8081
   - Order Service: http://localhost:8082
   - Payment Service: http://localhost:8083

---

## Place Service (порт 8081)

### 1. Отримати всі вільні місця
```bash
curl -X GET http://localhost:8081/api/places/free
```

**Очікувана відповідь:**
```json
[
  {
    "rackId": "r102",
    "sectionCode": "A",
    "number": 102,
    "type": "STANDARD",
    "status": "FREE",
    "pricePerDay": 50.00,
    "dimensions": {
      "width": 200,
      "height": 300,
      "depth": 100
    },
    "tenantId": null
  }
]
```

---

### 2. Отримати вільні місця за типом
```bash
# STANDARD
curl -X GET http://localhost:8081/api/places/free/type/STANDARD

# REFRIGERATED
curl -X GET http://localhost:8081/api/places/free/type/REFRIGERATED

# SECURE
curl -X GET http://localhost:8081/api/places/free/type/SECURE
```

---

### 3. Отримати місця користувача
```bash
curl -X GET http://localhost:8081/api/places/user/u001
```

**Очікувана відповідь:**
```json
[
  {
    "rackId": "r101",
    "sectionCode": "A",
    "number": 101,
    "type": "STANDARD",
    "status": "OCCUPIED",
    "pricePerDay": 50.00,
    "dimensions": {
      "width": 200,
      "height": 300,
      "depth": 100
    },
    "tenantId": "u001"
  }
]
```

---

### 4. Видати місця користувачу
```bash
curl -X POST http://localhost:8081/api/places/give \
  -H "Content-Type: application/json" \
  -d '{
    "placeIds": ["r102", "r201"],
    "userId": "u002"
  }'
```

**Очікувана відповідь:** HTTP 200 OK

---

### 5. Звільнити місця
```bash
curl -X POST http://localhost:8081/api/places/free \
  -H "Content-Type: application/json" \
  -d '["r102", "r201"]'
```

**Очікувана відповідь:** HTTP 200 OK

---

## Order Service (порт 8082)

### 1. Створити замовлення
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "u001",
    "rackCount": 2,
    "desiredType": "STANDARD",
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  }'
```

**Очікувана відповідь:**
```json
{
  "orderId": "ord12345678",
  "userId": "u001",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "rackCount": 2,
  "desiredType": "STANDARD",
  "assignedRacks": null,
  "status": "CREATED"
}
```

---

### 2. Отримати замовлення за діапазоном дат
```bash
curl -X GET "http://localhost:8082/api/orders/date-range?startDate=2024-01-01&endDate=2024-01-31"
```

**Очікувана відповідь:**
```json
[
  {
    "orderId": "ord12345678",
    "userId": "u001",
    "startDate": "2024-01-01",
    "endDate": "2024-01-31",
    "rackCount": 2,
    "desiredType": "STANDARD",
    "assignedRacks": ["r102", "r201"],
    "status": "CONFIRMED"
  }
]
```

---

### 3. Затвердити замовлення
```bash
curl -X PUT http://localhost:8082/api/orders/ord12345678/confirm
```

**Примітка:** Замовлення можна затвердити тільки якщо:
- Статус замовлення = CREATED
- Існує оплата для цього замовлення
- Достатньо вільних місць відповідного типу

**Очікувана відповідь:**
```json
{
  "orderId": "ord12345678",
  "userId": "u001",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "rackCount": 2,
  "desiredType": "STANDARD",
  "assignedRacks": ["r102", "r201"],
  "status": "CONFIRMED"
}
```

---

### 4. Скасувати замовлення
```bash
curl -X PUT http://localhost:8082/api/orders/ord12345678/cancel
```

**Очікувана відповідь:**
```json
{
  "orderId": "ord12345678",
  "userId": "u001",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "rackCount": 2,
  "desiredType": "STANDARD",
  "assignedRacks": null,
  "status": "CANCELLED"
}
```

---

### 5. Почати замовлення
```bash
curl -X PUT http://localhost:8082/api/orders/ord12345678/start
```

**Примітка:** Замовлення можна почати тільки якщо статус = CONFIRMED

**Очікувана відповідь:**
```json
{
  "orderId": "ord12345678",
  "userId": "u001",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "rackCount": 2,
  "desiredType": "STANDARD",
  "assignedRacks": ["r102", "r201"],
  "status": "ACTIVE"
}
```

---

### 6. Завершити замовлення
```bash
curl -X PUT http://localhost:8082/api/orders/ord12345678/finish
```

**Примітка:** Замовлення можна завершити тільки якщо статус = ACTIVE

**Очікувана відповідь:**
```json
{
  "orderId": "ord12345678",
  "userId": "u001",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "rackCount": 2,
  "desiredType": "STANDARD",
  "assignedRacks": null,
  "status": "FINISHED"
}
```

---

## Payment Service (порт 8083)

### 1. Створити оплату
```bash
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ord12345678",
    "userId": "u001",
    "amount": 1500.00,
    "date": "2024-01-01T10:30:00"
  }'
```

**Очікувана відповідь:**
```json
{
  "paymentId": "p12345678",
  "orderId": "ord12345678",
  "userId": "u001",
  "amount": 1500.00,
  "date": "2024-01-01T10:30:00"
}
```

---

### 2. Отримати всі оплати
```bash
curl -X GET http://localhost:8083/api/payments
```

**Очікувана відповідь:**
```json
[
  {
    "paymentId": "p12345678",
    "orderId": "ord12345678",
    "userId": "u001",
    "amount": 1500.00,
    "date": "2024-01-01T10:30:00"
  }
]
```

---

### 3. Отримати оплати за замовленням
```bash
curl -X GET http://localhost:8083/api/payments/order/ord12345678
```

**Очікувана відповідь:**
```json
[
  {
    "paymentId": "p12345678",
    "orderId": "ord12345678",
    "userId": "u001",
    "amount": 1500.00,
    "date": "2024-01-01T10:30:00"
  }
]
```

---

### 4. Отримати оплати користувача
```bash
curl -X GET http://localhost:8083/api/payments/user/u001
```

**Очікувана відповідь:**
```json
[
  {
    "paymentId": "p12345678",
    "orderId": "ord12345678",
    "userId": "u001",
    "amount": 1500.00,
    "date": "2024-01-01T10:30:00"
  }
]
```

---

## Типові сценарії тестування

### Сценарій 1: Повний цикл створення та обробки замовлення

```bash
# 1. Перевірити вільні місця
curl -X GET http://localhost:8081/api/places/free/type/STANDARD

# 2. Створити замовлення
ORDER_ID=$(curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "u001",
    "rackCount": 1,
    "desiredType": "STANDARD",
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  }' | jq -r '.orderId')

echo "Created order: $ORDER_ID"

# 3. Створити оплату для замовлення
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d "{
    \"orderId\": \"$ORDER_ID\",
    \"userId\": \"u001\",
    \"amount\": 1550.00,
    \"date\": \"2024-01-01T10:30:00\"
  }"

# 4. Затвердити замовлення
curl -X PUT http://localhost:8082/api/orders/$ORDER_ID/confirm

# 5. Почати замовлення
curl -X PUT http://localhost:8082/api/orders/$ORDER_ID/start

# 6. Перевірити місця користувача
curl -X GET http://localhost:8081/api/places/user/u001

# 7. Завершити замовлення
curl -X PUT http://localhost:8082/api/orders/$ORDER_ID/finish

# 8. Перевірити, що місця звільнені
curl -X GET http://localhost:8081/api/places/user/u001
```

---

### Сценарій 2: Скасування замовлення

```bash
# 1. Створити замовлення
ORDER_ID=$(curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "u002",
    "rackCount": 1,
    "desiredType": "REFRIGERATED",
    "startDate": "2024-02-01",
    "endDate": "2024-02-05"
  }' | jq -r '.orderId')

# 2. Створити оплату
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d "{
    \"orderId\": \"$ORDER_ID\",
    \"userId\": \"u002\",
    \"amount\": 482.00,
    \"date\": \"2024-02-01T09:00:00\"
  }"

# 3. Затвердити замовлення
curl -X PUT http://localhost:8082/api/orders/$ORDER_ID/confirm

# 4. Скасувати замовлення
curl -X PUT http://localhost:8082/api/orders/$ORDER_ID/cancel

# 5. Перевірити, що місця звільнені
curl -X GET http://localhost:8081/api/places/free/type/REFRIGERATED
```

---

## Примітки для тестування

### Типи місць (Rack Types)
- `STANDARD` - стандартне місце
- `REFRIGERATED` - холодильне місце
- `SECURE` - безпечне місце

### Статуси замовлень (Order Status)
- `CREATED` - створено
- `CONFIRMED` - затверджено
- `ACTIVE` - активне
- `FINISHED` - завершено
- `CANCELLED` - скасовано

### Статуси місць (Place Status)
- `FREE` - вільне
- `OCCUPIED` - зайняте

### Обробка помилок

**HTTP 400 Bad Request:**
- Невірні дані в запиті
- Неможливий перехід статусу замовлення
- Недостатньо вільних місць

**HTTP 500 Internal Server Error:**
- Помилка сервера
- Недоступність залежних сервісів (для Order Service)

### Тестування з Postman

1. Імпортуйте запити в Postman
2. Створіть змінні середовища:
   - `place_service_url`: http://localhost:8081
   - `order_service_url`: http://localhost:8082
   - `payment_service_url`: http://localhost:8083
3. Використовуйте змінні в URL: `{{place_service_url}}/api/places/free`

### Тестування з HTTP файлами (VS Code REST Client)

Створіть файл `api-requests.http`:

```http
### Place Service
GET http://localhost:8081/api/places/free

### Order Service
POST http://localhost:8082/api/orders
Content-Type: application/json

{
  "userId": "u001",
  "rackCount": 1,
  "desiredType": "STANDARD",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

---

## Перевірка доступності сервісів

```bash
# Place Service
curl -X GET http://localhost:8081/api/places/free

# Order Service (через Place Service виклик)
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "u001",
    "rackCount": 1,
    "desiredType": "STANDARD",
    "startDate": "2024-01-01",
    "endDate": "2024-01-31"
  }'

# Payment Service
curl -X GET http://localhost:8083/api/payments
```

---

## Додаткові інструменти

### jq для обробки JSON (опціонально)
```bash
# Встановлення jq (Linux/Mac)
# Ubuntu/Debian: sudo apt-get install jq
# Mac: brew install jq

# Приклад використання
curl -s http://localhost:8081/api/places/free | jq '.[0].rackId'
```

### HTTPie (альтернатива curl)
```bash
# Встановлення: pip install httpie

# Приклад
http GET localhost:8081/api/places/free
http POST localhost:8082/api/orders userId=u001 rackCount=1 desiredType=STANDARD startDate=2024-01-01 endDate=2024-01-31
```

