-- Создание таблицы пользователей
CREATE TABLE users (
                     user_id BIGINT PRIMARY KEY,
                       username VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       registration_date TIMESTAMP,
                        country VARCHAR(255),
                        league VARCHAR(255),
                        rank VARCHAR(255)
);

-- Таблица Users
CREATE TABLE Users (
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

-- Таблица Topics
CREATE TABLE Topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица User_Topics
CREATE TABLE User_Topics (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id) ON DELETE CASCADE,
    topic_id BIGINT REFERENCES Topics(id) ON DELETE CASCADE,
    selected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица Content
CREATE TABLE Content (
    id BIGSERIAL PRIMARY KEY,
    topic_id BIGINT REFERENCES Topics(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    content_data TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица Tests
CREATE TABLE Tests (
    id BIGSERIAL PRIMARY KEY,
    content_type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50),
    questions JSONB,
    answers JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица User_Test_Results
CREATE TABLE User_Test_Results (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id) ON DELETE CASCADE,
    test_id BIGINT REFERENCES Tests(id) ON DELETE CASCADE,
    score INT,
    time FLOAT,
    count INT,
    first_completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица Activity_Log
CREATE TABLE Activity_Log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES Users(id) ON DELETE CASCADE,
    action VARCHAR(255) NOT NULL,
    details JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);