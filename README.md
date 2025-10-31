# <span style="font-size:1.6em">🤖 Bishop Prototype with Synthetic Human Core Starter</span>

<p style="font-size:1.05em">
  Добро пожаловать! Этот репозиторий содержит прототип системы управления командами с очередью и воркерами
  на базе Spring Boot, а также стартер для аудита вызовов методов через AOP. Проект структурирован как
  несколько независимых Maven-модулей/проектов, которые можно собирать и запускать отдельно.
</p>

<hr/>

## 🔎 Обзор

- **Основной модуль (root):** `synthetic-human-core-starter` — Spring Boot приложение + библиотека-стартер с:
  - AOP-аудитом вызовов методов через аннотацию `@WeylandWatchingYou`;
  - Очередью команд (`CommandQueue`) и диспетчером-воркером (`CommandWorker`) на `ThreadPoolExecutor`;
  - Валидацией входных данных (Jakarta Validation);
  - Глобальной обработкой исключений.
- **Модуль:** `bishop-prototype` — минимальное Spring Boot-приложение (демо/заготовка).
- **Модуль:** `bi` — заготовка Maven-проекта (без кода, под будущие BI-компоненты).

<hr/>

## 🧱 Структура репозитория

```
.
├─ pom.xml                         # Spring Boot стартер + приложение (root)
├─ src/main/java/org/zuzukov/synthetichumancorestarter/
│  ├─ SyntheticHumanCoreStarterApplication.java
│  ├─ audit/
│  │  ├─ Aspect.java               # AOP аспект: аудит методов
│  │  ├─ AuditProperties.java      # Настройки аудита (CONSOLE/KAFKA)
│  │  └─ WeylandWatchingYou.java   # Аннотация для аудита
│  ├─ command/
│  │  ├─ Command.java              # Модель команды (валидация)
│  │  ├─ CommandProcessor.java     # Обработка команд (CRITICAL/COMMON)
│  │  ├─ CommandQueue.java         # Очередь команд
│  │  ├─ CommandWorker.java        # Диспетчер-воркер (ThreadPoolExecutor)
│  │  └─ Priority.java             # Приоритеты команд
│  └─ error/
│     ├─ GlobalExceptionHandler.java
│     └─ QueueOverflowException.java
│
├─ bishop-prototype/
│  ├─ pom.xml
│  └─ src/main/resources/application.properties
│
└─ bi/
   └─ pom.xml
```

<hr/>

## ✨ Возможности

- **AOP-аудит**: логирование вызовов методов, их аргументов и результата по аннотации `@WeylandWatchingYou`.
- **Очередь команд**: безопасная очередь с обработкой и переотправкой при переполнении пула.
- **Воркеры**: многопоточная обработка через `ThreadPoolExecutor` с бэкофом при `RejectedExecutionException`.
- **Валидация**: Jakarta Validation (`@NotBlank`, `@Size`, `@NotNull`) для модели `Command`.
- **Глобальная обработка ошибок**: единый `@RestControllerAdvice` для стандартизации ответов.

<hr/>

## 🧩 Технологии

- **Java**: 17 (root) / 21 (модули) — рекомендуется Java 21 для совместимости
- **Spring Boot**: 3.5.x (parent) / плагины 3.2.x указаны в свойствах
- **AOP**, **Validation**, **Spring Web**
- **Micrometer** (подключен), **Kafka clients** (для будущего аудита в Kafka)
- **Lombok**
- **Maven**

<hr/>

## 🚀 Быстрый старт

### 1) Предусловия
- Java 21 (JDK 21)
- Maven 3.9+

Проверьте версии:
```bash
java -version
mvn -v
```

### 2) Сборка
Собрать каждый модуль можно отдельно, так как они независимы:
```bash
# В корне (соберёт root-модуль synthetic-human-core-starter)
mvn clean package

# Собрать bishop-prototype
cd bishop-prototype && mvn clean package

# Собрать bi
cd ../bi && mvn clean package
```

### 3) Запуск
- Запуск основного приложения (root):
```bash
# из корня репозитория
mvn spring-boot:run
```
При старте `SyntheticHumanCoreStarterApplication` создаёт несколько команд и отправляет их в обработку `CommandProcessor` → `CommandQueue` → `CommandWorker`.

- Запуск `bishop-prototype` (минимальная заготовка):
```bash
cd bishop-prototype
mvn spring-boot:run
```

<hr/>

## ⚙️ Конфигурация

Настройки аудита в root-модуле управляются через `AuditProperties`:

```properties
# application.properties
weyland.audit.audit-type=CONSOLE   # CONSOLE или KAFKA
weyland.audit.kafka-topic=weyland.audit
```

По умолчанию включён режим **CONSOLE** — аудит выводится в логи.

<hr/>

## 🛠 Как подключить аудит к методу

Отметьте метод аннотацией `@WeylandWatchingYou` — AOP-аспект залогирует имя класса/метода, аргументы и результат:

```java
import org.zuzukov.synthetichumancorestarter.audit.WeylandWatchingYou;

public class MyService {

    @WeylandWatchingYou
    public String doWork(String param) {
        return "result:" + param;
    }
}
```

<hr/>

## 🧠 Внутренние детали

- **Command** — объект команды с валидацией полей (`description`, `priority`, `author`, `time`).
- **CommandProcessor** — отправляет CRITICAL-команды сразу в лог, остальные — в очередь.
- **CommandQueue** — двусторонняя очередь. В случае переполнения выбрасывает `QueueOverflowException`.
- **CommandWorker** — отдельный поток, постоянно читает из очереди и отправляет задачи в `ThreadPoolExecutor` (2–4 потока). При `RejectedExecutionException` делает паузу и возвращает команду в начало очереди.
- **GlobalExceptionHandler** — общий обработчик для ошибок валидации, переполнения очереди и прочих исключений.

<hr/>

## 🧪 Тестирование

Запуск тестов модуля:
```bash
mvn test          # в корне — для root-модуля
cd bishop-prototype && mvn test
cd ../bi && mvn test
```

<hr/>

## 📦 Сборка артефактов

Результаты сборки появятся в `target/` каждого модуля. Для root-модуля после `mvn package` вы получите исполняемый `jar`.

<hr/>

## 📜 Лицензия

Добавьте информацию о лицензии (при необходимости).

<hr/>

## 🙌 Благодарности

- Spring, AOP, и вдохновение от идеи «наблюдающего» Weyland 👁️

<p align="center" style="font-size:1.1em">
  <b>Удачной разработки!</b> ✨ Если понадобятся примеры контроллеров/API — дайте знать, добавлю.
</p>
