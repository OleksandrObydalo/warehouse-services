# OpenAPI Документація та Контракти

## Що таке OpenAPI?

**OpenAPI** (раніше Swagger) - це стандартний формат для опису REST API у форматі JSON або YAML. Він дозволяє:
- Автоматично генерувати документацію API
- Створювати клієнтські бібліотеки
- Тестувати API через інтерактивний UI
- Валідувати запити та відповіді

## Чому OpenAPI, а не WADL?

| Критерій | WADL | OpenAPI |
|---------|------|---------|
| **Формат** | XML (застарілий) | JSON/YAML (сучасний) |
| **Підтримка** | Обмежена | Широка, стандарт індустрії |
| **Інструменти** | Мало | Багато (Swagger UI, Postman, тощо) |
| **Автогенерація** | Складна | Легка з SpringDoc |
| **Інтерактивне тестування** | Немає | Swagger UI |
| **Версіонування** | Не підтримується | Підтримується |

**Висновок**: OpenAPI - це сучасний стандарт, який використовується в більшості проєктів.

---

## Як отримати OpenAPI контракти

### 1. Через Swagger UI (Інтерактивний інтерфейс)

Після запуску сервісів, відкрийте в браузері:

#### Place Service
```
http://localhost:8081/swagger-ui.html
```

#### Order Service
```
http://localhost:8082/swagger-ui.html
```

#### Payment Service
```
http://localhost:8083/swagger-ui.html
```

У Swagger UI ви можете:
- Переглянути всі endpoint'и
- Побачити схеми запитів та відповідей
- Протестувати API безпосередньо в браузері
- Завантажити OpenAPI специфікацію

### 2. Через API Endpoint (JSON формат)

#### Place Service
```bash
curl http://localhost:8081/api-docs
```

Або в браузері:
```
http://localhost:8081/api-docs
```

#### Order Service
```bash
curl http://localhost:8082/api-docs
```

Або в браузері:
```
http://localhost:8082/api-docs
```

#### Payment Service
```bash
curl http://localhost:8083/api-docs
```

Або в браузері:
```
http://localhost:8083/api-docs
```

### 3. Завантажити через Swagger UI

1. Відкрийте Swagger UI для потрібного сервісу
2. Знайдіть кнопку "Download" або посилання на JSON/YAML
3. Або скопіюйте вміст з `/api-docs` endpoint

### 4. Експорт через curl

```bash
# Place Service
curl http://localhost:8081/api-docs > place-service-openapi.json

# Order Service
curl http://localhost:8082/api-docs > order-service-openapi.json

# Payment Service
curl http://localhost:8083/api-docs > payment-service-openapi.json
```

---

## Структура OpenAPI контракту

OpenAPI контракт містить:

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "Place Service API",
    "description": "API для управління складськими місцями",
    "version": "1.0.0"
  },
  "servers": [
    {
      "url": "http://localhost:8081",
      "description": "Place Service"
    }
  ],
  "paths": {
    "/api/places/free": {
      "get": {
        "summary": "Отримати всі вільні місця",
        "responses": {
          "200": {
            "description": "Список вільних місць",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/PlaceDTO"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "PlaceDTO": {
        "type": "object",
        "properties": {
          "rackId": {
            "type": "string"
          },
          "type": {
            "type": "string",
            "enum": ["STANDARD", "REFRIGERATED", "SECURE"]
          }
        }
      }
    }
  }
}
```

---

## Використання OpenAPI контрактів

### 1. Генерація клієнтських бібліотек

Використовуючи OpenAPI Generator:

```bash
# Встановлення OpenAPI Generator
npm install @openapitools/openapi-generator-cli -g

# Генерація Java клієнта
openapi-generator-cli generate \
  -i http://localhost:8081/api-docs \
  -g java \
  -o ./generated-client/place-service-client

# Генерація TypeScript клієнта
openapi-generator-cli generate \
  -i http://localhost:8081/api-docs \
  -g typescript-axios \
  -o ./generated-client/place-service-client-ts
```

### 2. Імпорт в Postman

1. Відкрийте Postman
2. Натисніть "Import"
3. Виберіть "Link" та введіть URL: `http://localhost:8081/api-docs`
4. Або завантажте JSON файл

### 3. Валідація запитів

Використовуючи бібліотеки валідації:

```java
// Приклад використання в Java
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;

OpenApiCompare compare = new OpenApiCompare();
ChangedOpenApi diff = compare.compare(oldSpec, newSpec);
```

### 4. Документація для команди

OpenAPI контракти можна використовувати як:
- Живу документацію (Swagger UI)
- Контракт між командами
- Базу для тестування
- Специфікацію для нових розробників

---

## Налаштування OpenAPI в проєкті

### Конфігурація в application.properties

```properties
# Шлях до OpenAPI JSON
springdoc.api-docs.path=/api-docs

# Шлях до Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html

# Сортування операцій
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

### Додавання описів до endpoint'ів

Використовуйте анотації в контролерах:

```java
@Operation(
    summary = "Отримати всі вільні місця",
    description = "Повертає список всіх вільних місць на складі"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Список вільних місць",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class))
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Внутрішня помилка сервера"
    )
})
@GetMapping("/free")
public ResponseEntity<List<PlaceDTO>> getAllFreePlaces() {
    // ...
}
```

---

## Порівняння з WADL

### WADL (Web Application Description Language)

**Недоліки:**
- Застарілий XML формат
- Обмежена підтримка в сучасних інструментах
- Складніша генерація документації
- Немає інтерактивного UI

**Приклад WADL:**
```xml
<application xmlns="http://wadl.dev.java.net/2009/02">
  <resources base="http://localhost:8081/api">
    <resource path="places">
      <method name="GET" id="getAllFreePlaces">
        <response>
          <representation mediaType="application/json"/>
        </response>
      </method>
    </resource>
  </resources>
</application>
```

### OpenAPI

**Переваги:**
- Сучасний JSON/YAML формат
- Широка підтримка інструментів
- Автоматична генерація з анотацій
- Інтерактивний Swagger UI
- Генерація клієнтських бібліотек
- Валідація запитів/відповідей

---

## Практичні приклади

### Приклад 1: Перегляд документації

```bash
# 1. Запустіть сервіси
./start-all-services.sh

# 2. Відкрийте Swagger UI в браузері
# Place Service: http://localhost:8081/swagger-ui.html
# Order Service: http://localhost:8082/swagger-ui.html
# Payment Service: http://localhost:8083/swagger-ui.html

# 3. Перегляньте всі endpoint'и та їх описи
# 4. Протестуйте API безпосередньо в браузері
```

### Приклад 2: Завантаження контрактів

```bash
# Завантажити всі контракти
curl http://localhost:8081/api-docs > place-service-openapi.json
curl http://localhost:8082/api-docs > order-service-openapi.json
curl http://localhost:8083/api-docs > payment-service-openapi.json

# Перевірити вміст
cat place-service-openapi.json | jq '.info'
```

### Приклад 3: Генерація клієнта

```bash
# Генерація Java клієнта для Place Service
openapi-generator-cli generate \
  -i place-service-openapi.json \
  -g java \
  -o ./clients/place-service-client \
  --library resttemplate
```

---

## Рекомендації

1. **Використовуйте OpenAPI** замість WADL для нових проєктів
2. **Додавайте детальні описи** до endpoint'ів через анотації
3. **Оновлюйте контракти** при зміні API
4. **Використовуйте версіонування** для OpenAPI специфікацій
5. **Експортуйте контракти** в систему контролю версій (Git)
6. **Використовуйте Swagger UI** для тестування під час розробки

---

## Корисні посилання

- [OpenAPI Specification](https://swagger.io/specification/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [OpenAPI Generator](https://openapi-generator.tech/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

---

## Висновок

**OpenAPI** - це сучасний стандарт для опису REST API, який:
- ✅ Автоматично генерується з коду
- ✅ Надає інтерактивний UI для тестування
- ✅ Дозволяє генерувати клієнтські бібліотеки
- ✅ Використовується в більшості сучасних проєктів

**WADL** - застарілий формат, який не рекомендується для нових проєктів.

