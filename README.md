# Образовательный телеграм-бот

Телеграм-бот для интерактивного обучения по различным предметам в стиле Duolingo. Пользователи могут выбрать интересующую тему, проходить уроки и квизы, зарабатывать опыт и отслеживать свой прогресс.

## Технологии

- Kotlin
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Security
- Swagger/OpenAPI

## Особенности

- Разделение материала по предметам и темам
- Различные типы вопросов (выбор из нескольких, да/нет, заполнение пропусков и другие)
- Система опыта и прогресса
- Отслеживание статистики по каждому пользователю
- REST API для интеграции с фронтендом

## Запуск проекта

### Требования

- JDK 17+
- PostgreSQL 14+
- Gradle 8+

### База данных

Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE quiz_db;
```

### Настройка

1. Клонируйте репозиторий
2. Настройте подключение к базе данных в `application-dev.yml`
3. Запустите приложение:

```bash
./gradlew bootRun
```

## Структура проекта

- `entity` - Модели данных для базы данных
- `repository` - Репозитории для работы с базой данных
- `service` - Бизнес-логика приложения
- `controller` - REST API контроллеры
- `dto` - Объекты для передачи данных
- `config` - Конфигурационные классы
- `exception` - Обработка исключений


## DDL

```
-- Таблица users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    telegram_id BIGINT UNIQUE NOT NULL,
    username VARCHAR(255),
    full_name VARCHAR(255),
    profile_data JSONB,
    rating INT DEFAULT 0,
    streak INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица topics
CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица user_topics
CREATE TABLE user_topics (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    topic_id BIGINT REFERENCES topics(id) ON DELETE CASCADE,
    selected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица content
CREATE TABLE content (
    id BIGSERIAL PRIMARY KEY,
    topic_id BIGINT REFERENCES topics(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    content_data TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица tests
CREATE TABLE tests (
    id BIGSERIAL PRIMARY KEY,
    content_type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50),
    questions JSONB,
    answers JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица user_test_results
CREATE TABLE user_test_results (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    test_id BIGINT REFERENCES tests(id) ON DELETE CASCADE,
    score INT,
    time FLOAT,
    count INT,
    first_completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица activity_log
CREATE TABLE activity_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    action VARCHAR(255) NOT NULL,
    details JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица tokens
CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    value VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP,
    revoked BOOLEAN DEFAULT false
);

-- Таблица users_authorization
CREATE TABLE users_authorization (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telegram_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(255),
    profile_data JSONB
);
```
## Ссылка на бота
```
@RootsUp_bot
```
