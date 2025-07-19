# 🧠 Bishop Prototype

**Bishop** — микросервис, реализующий API для обработки команд от синтетических агентов с возможностью аудита действий (в консоль или в Kafka). Построен на основе стартера `synthetic-human-core-starter`.

---

## 📦 Состав проекта

```
T1Task3/                         ← родительский Maven-проект
├── synthetic-human-core-starter ← стартер с бизнес-логикой и аудитом (as dependency)
└── bishop-prototype             ← ваш микросервис Bishop
```

---

## 🚀 Возможности

- REST API для получения команд от синтетиков
- Асинхронная очередь команд
- Приоритезация и валидация команд
- Аудит (в консоль или Kafka)
- Интеграция с Prometheus и Actuator
- OpenAPI документация (`/swagger-ui.html`)

---

## 📁 Основные компоненты

| Компонент                     | Назначение |
|------------------------------|------------|
| `CommandController`          | REST endpoint `/api/commands` для приёма команд |
| `ServiceCommand`             | Обёртка над `CommandProcessor`, делегирует обработку |
| `@WeylandWatchingYou`        | Аспект для аудита всех методов, помеченных аннотацией |
| `AuditKafkaListener`         | Слушает Kafka-топик и выводит сообщения аудита |
| `application.yaml`           | Конфигурация Spring, Kafka, аудит и метрики |
| `Dockerfile`                 | Двухэтапная сборка Bishop-сервиса |
| `docker-compose.yml`         | Kafka + Zookeeper + Bishop в одном окружении |

---

## 🧪 API

### 🔧 POST `/api/commands`

Создаёт новую команду.

#### Пример запроса:
```json
{
  "description": "Уничтожить цель до рассвета",
  "priority": "CRITICAL",
  "author": "T-800",
  "time": "2025-07-18T22:00:00Z"
}
```

#### Swagger UI:
- [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## 🛡️ Аудит

### 1. 📋 Консольный режим (по умолчанию)

```yaml
weyland:
  audit:
    audit-type: CONSOLE
```

Вывод:
```
[AUDIT - CONSOLE] Command accepted by T-800 with priority CRITICAL
```

### 2. 📡 Kafka-аудит

#### Переключение на Kafka:
```yaml
weyland:
  audit:
    audit-type: KAFKA
    kafka-topic: weyland.audit
```

#### Kafka Listener:
```text
[AUDIT FROM KAFKA] Command accepted by T-800 with priority CRITICAL
```

> ⚠️ Kafka listener активируется только при `audit-type: KAFKA`.

---

## 🧰 Запуск проекта

### 📍 1. Без Kafka (только с консольным аудитом)

```bash
# Сборка
mvn clean install

# Запуск
cd bishop-prototype
java -jar target/bishop-prototype-0.0.1-SNAPSHOT.jar
```

### 📡 2. С Kafka (docker-compose)

```bash
docker-compose up --build
```

---

## 📊 Метрики

```yaml
scrape_configs:
  - job_name: 'synthetic-bishop'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

---

## ⚙️ Переменные окружения (Docker)

```yaml
environment:
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  WEYLAND_AUDIT_AUDIT_TYPE: KAFKA
  WEYLAND_AUDIT_KAFKA_TOPIC: weyland.audit
```

---

## 🧪 Тестирование

Планируется внедрение unit/integration тестов.

---

## 👨‍💻 Авторы

- Проект разработан в рамках синтетической архитектуры Weyland

---

## 🧩 Лицензия

MIT или подходящая корпоративная.