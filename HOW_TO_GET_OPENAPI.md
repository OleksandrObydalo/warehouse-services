# Як отримати OpenAPI контракти

## Швидкий старт

### 1. Запустіть сервіси
```bash
./start-all-services.sh
# або
start-all-services.bat
```

### 2. Відкрийте Swagger UI в браузері

**Place Service:**
```
http://localhost:8081/swagger-ui.html
```

**Order Service:**
```
http://localhost:8082/swagger-ui.html
```

**Payment Service:**
```
http://localhost:8083/swagger-ui.html
```

### 3. Отримайте OpenAPI JSON

**Через браузер:**
- Place Service: http://localhost:8081/api-docs
- Order Service: http://localhost:8082/api-docs
- Payment Service: http://localhost:8083/api-docs

**Через curl:**
```bash
# Place Service
curl http://localhost:8081/api-docs > place-service-openapi.json

# Order Service
curl http://localhost:8082/api-docs > order-service-openapi.json

# Payment Service
curl http://localhost:8083/api-docs > payment-service-openapi.json
```

## Що далі?

1. **Перегляньте документацію** в Swagger UI
2. **Протестуйте API** безпосередньо в браузері
3. **Завантажте контракти** для використання в інших інструментах
4. **Імпортуйте в Postman** або інші API клієнти

Детальніше: [OPENAPI_DOCUMENTATION.md](OPENAPI_DOCUMENTATION.md)

