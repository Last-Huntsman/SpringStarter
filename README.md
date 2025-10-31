<div align="center">
  <h1 style="font-family: 'Segoe UI', Roboto, Arial; color:#6C63FF; font-weight:800; letter-spacing:0.5px;">
    🧠 Bishop Prototype
  </h1>
  <p style="font-size:16px; color:#444; max-width:820px; line-height:1.6;">
    <b>Bishop</b> — микросервис, принимающий и обрабатывающий команды от синтетических агентов с аудированием действий
    (консоль или Kafka), метриками и OpenAPI. Построен на собственном стартере <code>synthetic-human-core-starter</code>.
  </p>
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Kafka-7.6.0-231F20?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Build-Maven-blue?style=for-the-badge" />
</div>

<br/>

## ✨ Что внутри

- ✅ **REST API** для создания команд (`/api/commands`)
- ✅ **Очередь команд** с лимитом и возможностью приоритезации
- ✅ **Аудит**: консольный и через **Kafka** (включаемый конфигурацией)
- ✅ **OpenAPI/Swagger UI**: `http://localhost:8080/swagger-ui.html`
- ✅ **Actuator + Prometheus** метрики: `/actuator/prometheus`

---

## 🧱 Модули проекта

```text
T1Task3/                         ← родительский Maven-проект (Java 21, Spring Boot 3.5.3)
├── synthetic-human-core-starter ← стартер: аннотация и аспект аудита, очередь/процессор команд, метрики
└── bishop-prototype             ← микросервис: REST-контроллер, сервис, Kafka listener, конфиги
```

### Ключевые классы

- `bishop-prototype`
  - `CommandController` — POST `/api/commands`, принимает валидируемую модель `Command`, аннотирован `@WeylandWatchingYou`.
  - `ServiceCommand` — делегирует обработку в `CommandProcessor` (стартера), тоже аудируется.
  - `AuditKafkaListener` — слушает топик `${weyland.audit.kafka-topic}` при `weyland.audit.audit-type=KAFKA`.

- `synthetic-human-core-starter`
  - `@WeylandWatchingYou` — маркер-аннотация для аудита.
  - `AuditAspect` — вокруг вызова: логирует в консоль или отправляет в Kafka.
  - `Command`, `Priority`, `CommandQueue`, `CommandProcessor` — модель и обработка команд, метрики через Micrometer.

---

## 🔌 API

### POST `/api/commands`

Создаёт новую команду.

Пример тела запроса:

```json
{
  "description": "Уничтожить цель до рассвета",
  "priority": "CRITICAL",
  "author": "T-800",
  "time": "2025-07-18T22:00:00Z"
}
```

- Swagger UI: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## 🛡️ Аудит (console / kafka)

Конфигурация в `application.yml`:

```yaml
weyland:
  audit:
    audit-type: CONSOLE   # или KAFKA
    kafka-topic: weyland.audit
```

- При `CONSOLE` выводится лог уровня INFO, формируемый `AuditAspect`.
- При `KAFKA` используется `AuditKafkaSender` (стартер) и `AuditKafkaListener` (микросервис) для чтения сообщений.

Пример лога из Kafka listener:

```text
[ AUDIT FROM KAFKA] [AUDIT] Class: ... Method: ..., Args: [...], Result: ...
```

> ⚠️ Listener активируется условно по свойству `weyland.audit.audit-type=KAFKA`.

---

## 📊 Метрики и здоровье

- Actuator доступен по `/actuator` (в `application.yml` включён `exposure: "*"`).
- Prometheus scrape-джоб (см. `bishop-prototype/src/main/resources/prometheus.yml`):

```yaml
scrape_configs:
  - job_name: 'synthetic-bishop'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

---

## 🧰 Сборка и запуск

### Вариант A: Локально (консольный аудит по умолчанию)

```bash
mvn clean install
cd bishop-prototype
java -jar target/bishop-prototype-0.0.1-SNAPSHOT.jar
```

### Вариант B: С Kafka (Docker Compose)

```bash
docker-compose up --build
```

Переменные окружения сервиса в Compose:

```yaml
SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
WEYLAND_AUDIT_AUDIT_TYPE: KAFKA
WEYLAND_AUDIT_KAFKA_TOPIC: weyland.audit
```

Dockerfile использует двухэтапную сборку (Maven build → runtime JDK 21):

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
RUN mvn -pl bishop-prototype -am clean package -DskipTests
...
```

---

## ⚙️ Конфигурация (ключевые свойства)

`bishop-prototype/src/main/resources/application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: auditor
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer

weyland:
  audit:
    audit-type: CONSOLE
    kafka-topic: weyland.audit

management:
  endpoints:
    web:
      exposure:
        include: "*"
  metrics:
    tags:
      application: synthetic-bishop
```

---

## 🧭 Архитектура (обзор)

```text
Client → REST (CommandController) → ServiceCommand → CommandProcessor (starter)
                                           │                  │
                                           │        ┌─────────┴─────────┐
                                           │        │    AuditAspect    │
                                           │        └─────────┬─────────┘
                                           │                  │
                                           ▼                  ▼
                                     Queue/Execute      Console or Kafka
                                                           │
                                                           ▼
                                                  AuditKafkaListener (when KAFKA)
```

---

## 🧪 Тестирование

- Юнит/интеграционные тесты могут быть добавлены для контроллера и процессора команд.
- В текущей версии часть модульных тестов отключена в стартере (Surefire.skipTests=true), что ускоряет сборку.

---

## 🧾 Зависимости и версии

- Java: 21
- Spring Boot: 3.5.3
- OpenAPI: `springdoc-openapi-starter-webmvc-ui` 2.8.8
- Kafka: `spring-kafka`
- Micrometer + Prometheus

---

## 👨‍💻 Авторы

- Проект разработан в рамках синтетической архитектуры Weyland.

---

## 🧩 Лицензия

MIT или подходящая корпоративная.

<div align="center" style="margin-top:16px; color:#888;">
  Сделано с ❤️ и <span style="color:#6C63FF; font-weight:700;">AOP</span> ✨
</div>
