# Warehouse Management System - Microservices Architecture

Розподілений REST застосунок для управління складськими місцями з архітектурою мікросервісів.

## Структура проєкту

Проєкт складається з трьох мікросервісів та консольного клієнта:

1. **Place Service** (порт 8081) - управління складськими місцями
2. **Order Service** (порт 8082) - управління замовленнями
3. **Payment Service** (порт 8083) - управління оплатами
4. **Console Client** - консольний клієнт для взаємодії з системою

## Вимоги

- Java 17 або вище
- Maven 3.6+
- Windows або Linux/MacOS

## Запуск системи

### Windows

1. Запустіть всі мікросервіси:
   ```batch
   start-all-services.bat
   ```

2. Після запуску всіх сервісів (зачекайте ~30 секунд), запустіть консольний клієнт:
   ```batch
   start-console-client.bat
   ```

### Linux/MacOS

1. Надайте права на виконання:
   ```bash
   chmod +x start-all-services.sh start-console-client.sh
   ```

2. Запустіть всі мікросервіси:
   ```bash
   ./start-all-services.sh
   ```

3. Після запуску всіх сервісів, запустіть консольний клієнт:
   ```bash
   ./start-console-client.sh
   ```

### Ручний запуск

Альтернативно, можна запустити кожен сервіс окремо:

```bash
# Place Service
cd place-service
mvn spring-boot:run

# Order Service (в новому терміналі)
cd order-service
mvn spring-boot:run

# Payment Service (в новому терміналі)
cd payment-service
mvn spring-boot:run

# Console Client (в новому терміналі)
cd console-client
mvn spring-boot:run
```

## OpenAPI Документація

Кожен мікросервіс має автоматично згенеровану OpenAPI документацію:

### Swagger UI (Інтерактивний інтерфейс)
- **Place Service**: http://localhost:8081/swagger-ui.html
- **Order Service**: http://localhost:8082/swagger-ui.html
- **Payment Service**: http://localhost:8083/swagger-ui.html

### OpenAPI JSON контракти
- **Place Service**: http://localhost:8081/api-docs
- **Order Service**: http://localhost:8082/api-docs
- **Payment Service**: http://localhost:8083/api-docs

У Swagger UI ви можете:
- Переглянути всі endpoint'и з детальними описами
- Побачити схеми запитів та відповідей
- Протестувати API безпосередньо в браузері
- Завантажити OpenAPI специфікацію

Детальніше про OpenAPI див. [OPENAPI_DOCUMENTATION.md](OPENAPI_DOCUMENTATION.md)

---

## API Endpoints

### Place Service (http://localhost:8081)

- `GET /api/places/free` - отримати всі вільні місця
- `GET /api/places/free/type/{type}` - отримати вільні місця за типом
- `GET /api/places/user/{userId}` - отримати місця користувача
- `POST /api/places/give` - видати місця користувачу
- `POST /api/places/free` - звільнити місця

### Order Service (http://localhost:8082)

- `POST /api/orders` - створити замовлення
- `GET /api/orders/date-range?startDate={date}&endDate={date}` - отримати замовлення за діапазоном дат
- `PUT /api/orders/{orderId}/confirm` - затвердити замовлення
- `PUT /api/orders/{orderId}/cancel` - скасувати замовлення
- `PUT /api/orders/{orderId}/start` - почати замовлення
- `PUT /api/orders/{orderId}/finish` - завершити замовлення

### Payment Service (http://localhost:8083)

- `POST /api/payments` - створити оплату
- `GET /api/payments` - отримати всі оплати
- `GET /api/payments/user/{userId}` - отримати оплати користувача
- `GET /api/payments/order/{orderId}` - отримати оплати за замовленням

## Консольний клієнт

Після запуску консольного клієнта доступні наступні команди:

1. Get all free places - отримати всі вільні місця
2. Get places by user ID - отримати місця користувача
3. Create order - створити замовлення
4. Get orders by date range - отримати замовлення за діапазоном дат
5. Confirm order - затвердити замовлення
6. Cancel order - скасувати замовлення
7. Start order - почати замовлення
8. Finish order - завершити замовлення
9. Create payment - створити оплату
10. Get all payments - отримати всі оплати
11. Get payments by user ID - отримати оплати користувача
12. Get payments by order ID - отримати оплати за замовленням

Введіть `help` для показу меню або `exit` для виходу.

## Архітектура

### Тришарова архітектура

Кожен мікросервіс має три шари:

1. **Controller** - обробка HTTP запитів
2. **Service** - бізнес-логіка
3. **Repository** - доступ до даних

### DTO (Data Transfer Objects)

- **Міжшарові DTO** - використовуються для передачі даних між шарами всередині сервісу
- **Міжсервісні DTO** - використовуються для комунікації між мікросервісами

### Бази даних

Кожен мікросервіс має свою окрему базу даних (H2 in-memory):

- Place Service: `place_db`
- Order Service: `order_db`
- Payment Service: `payment_db`

### Міжсервісна комунікація

- **Синхронна комунікація** через REST API
- **Fail-fast** для критичних операцій (Place Service)
- **Fail-silent** для некритичних операцій (Payment Service при перевірці оплат)

### JSON Schema Validation

Використовуються JSON схеми для валідації міжсервісних контрактів:
- `order-service/src/main/resources/schemas/place-service-dto-schema.json`
- `order-service/src/main/resources/schemas/payment-service-dto-schema.json`

## Розподілені транзакції

При невдалому виклику між сервісами:

1. **Order Service → Place Service**: Використовується fail-fast підхід. Якщо Place Service недоступний, операція не виконується і повертається помилка.

2. **Order Service → Payment Service**: Використовується fail-silent підхід при перевірці оплат. Якщо Payment Service недоступний, система продовжує роботу без блокування.

3. **Компенсаційні транзакції**: При скасуванні або завершенні замовлення, місця автоматично звільняються через виклик Place Service.

## Конфігурація

Налаштування кожного сервісу знаходяться у файлах `application.properties`:
- Порти серверів
- URL інших сервісів
- Налаштування баз даних

## Тестування

Після запуску всіх сервісів, можна протестувати систему через консольний клієнт або напряму через HTTP запити (наприклад, за допомогою Postman або curl).

## Примітки

- Всі сервіси використовують H2 in-memory бази даних, тому дані не зберігаються між перезапусками
- Для production використання необхідно замінити H2 на повноцінну БД (PostgreSQL, MySQL тощо)
- Консольний клієнт використовує Spring Boot для dependency injection, але не потребує власної бази даних

